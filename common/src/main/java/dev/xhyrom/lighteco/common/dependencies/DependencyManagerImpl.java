package dev.xhyrom.lighteco.common.dependencies;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.MoreFiles;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.util.URLClassLoaderAccess;
import dev.xhyrom.lighteco.common.storage.StorageType;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class DependencyManagerImpl implements DependencyManager {
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);
    private final Map<ImmutableSet<Dependency>, IsolatedClassLoader> loaders = new HashMap<>();

    private final PluginLogger logger;
    private final DependencyRegistry registry;
    private final Path cacheDirectory;
    private final URLClassLoaderAccess classLoader;

    public DependencyManagerImpl(LightEcoPlugin plugin) {
        this.logger = plugin.getBootstrap().getLogger();
        this.registry = new DependencyRegistry();
        this.cacheDirectory = setupCacheDirectory(plugin);
        this.classLoader = URLClassLoaderAccess.create((URLClassLoader) plugin.getBootstrap().getClass().getClassLoader());
    }

    @Override
    public void loadDependencies(Set<Dependency> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                latch.countDown();
                continue;
            }

            CompletableFuture.runAsync(() -> {
                try {
                    loadDependency(dependency);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load dependency " + dependency, e);
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDependency(Dependency dependency) throws Exception {
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = downloadDependency(dependency);

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

    @Override
    public void loadStorageDependencies(Set<StorageType> types) {
        loadDependencies(this.registry.resolveStorageDependencies(types));
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
