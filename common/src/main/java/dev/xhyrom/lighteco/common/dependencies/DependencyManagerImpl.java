package dev.xhyrom.lighteco.common.dependencies;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.MoreFiles;

import dev.xhyrom.lighteco.common.config.Config;
import dev.xhyrom.lighteco.common.dependencies.relocation.Relocation;
import dev.xhyrom.lighteco.common.dependencies.relocation.RelocationHandler;
import dev.xhyrom.lighteco.common.messaging.MessagingType;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.storage.StorageType;
import dev.xhyrom.lighteco.common.util.URLClassLoaderAccess;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DependencyManagerImpl implements DependencyManager {
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);
    private final Map<ImmutableSet<Dependency>, IsolatedClassLoader> loaders = new HashMap<>();

    private final Config config;
    private final PluginLogger logger;
    private final DependencyRegistry registry;
    private final Path cacheDirectory;
    private final URLClassLoaderAccess classLoader;
    private @MonotonicNonNull RelocationHandler relocationHandler;

    public DependencyManagerImpl(LightEcoPlugin plugin) {
        this.config = plugin.getConfig();
        this.logger = plugin.getBootstrap().getLogger();
        this.registry = new DependencyRegistry();
        this.cacheDirectory = setupCacheDirectory(plugin);
        this.classLoader = URLClassLoaderAccess.create(
                (URLClassLoader) plugin.getBootstrap().getClass().getClassLoader());
    }

    private synchronized RelocationHandler getRelocationHandler() {
        if (this.relocationHandler == null) {
            this.relocationHandler = new RelocationHandler(this);
        }

        return this.relocationHandler;
    }

    @Override
    public void loadDependencies(Set<Dependency> dependencies) {
        if (this.config.debug) this.logger.info("Loading dependencies: " + dependencies);

        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                continue;
            }

            if (this.config.debug) this.logger.info("Loading dependency " + dependency);

            try {
                loadDependency(dependency);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load dependency " + dependency, e);
            } finally {
                if (this.config.debug) this.logger.info("Loaded dependency " + dependency);
            }
        }

        if (this.config.debug) this.logger.info("Loaded dependencies: " + dependencies);
    }

    private void loadDependency(Dependency dependency) throws Exception {
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = remapDependency(dependency, downloadDependency(dependency));

        this.loaded.put(dependency, file);

        if (this.registry.shouldAutoLoad(dependency)) {
            try {
                this.classLoader.addURL(file.toUri().toURL());
            } catch (Exception e) {
                throw new RuntimeException("Failed to load dependency " + dependency, e);
            }
        }
    }

    private Path downloadDependency(Dependency dependency) throws Exception {
        Path file = this.cacheDirectory.resolve(dependency.getFileName());

        if (Files.exists(file)) {
            return file;
        }

        Files.createDirectories(file.getParent());

        DependencyRepository.MAVEN_CENTRAL.download(dependency, file);

        return file;
    }

    private Path remapDependency(Dependency dependency, Path normalFile) throws Exception {
        List<Relocation> rules = new ArrayList<>(dependency.getRelocations());

        if (rules.isEmpty()) {
            return normalFile;
        }

        Path remappedFile = this.cacheDirectory.resolve(dependency.getFileName("remapped"));

        // if the remapped source exists already, just use that.
        if (Files.exists(remappedFile)) {
            return remappedFile;
        }

        this.getRelocationHandler().remap(normalFile, remappedFile, rules);
        return remappedFile;
    }

    @Override
    public void loadStorageDependencies(Set<StorageType> types) {
        loadDependencies(this.registry.resolveStorageDependencies(types));
    }

    @Override
    public void loadMessagingDependencies(Set<MessagingType> types) {
        loadDependencies(this.registry.resolveMessagingDependencies(types));
    }

    @Override
    public ClassLoader obtainClassLoaderWith(Set<Dependency> dependencies) {
        ImmutableSet<Dependency> set = ImmutableSet.copyOf(dependencies);

        for (Dependency dependency : dependencies) {
            if (!this.loaded.containsKey(dependency)) {
                throw new IllegalStateException("Dependency " + dependency + " is not loaded");
            }
        }

        synchronized (this.loaders) {
            IsolatedClassLoader classLoader = this.loaders.get(set);
            if (classLoader != null) {
                return classLoader;
            }

            URL[] urls = set.stream()
                    .map(this.loaded::get)
                    .map(file -> {
                        try {
                            return file.toUri().toURL();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            classLoader = new IsolatedClassLoader(urls);
            this.loaders.put(set, classLoader);
            return classLoader;
        }
    }

    private static Path setupCacheDirectory(LightEcoPlugin plugin) {
        Path cacheDirectory = plugin.getBootstrap().getDataDirectory().resolve("libraries");

        try {
            MoreFiles.createParentDirectories(cacheDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create libraries (cache) directory", e);
        }

        return cacheDirectory;
    }

    @Override
    public void close() {
        IOException exception = null;

        for (IsolatedClassLoader loader : this.loaders.values()) {
            try {
                loader.close();
            } catch (Exception e) {
                if (exception == null) {
                    exception = new IOException("Failed to close class loader", e);
                } else {
                    exception.addSuppressed(e);
                }
            }
        }

        if (exception != null) {
            this.logger.error("Failed to close class loader", exception);
        }
    }
}
