package dev.xhyrom.lighteco.common.dependencies;

import dev.xhyrom.lighteco.common.storage.StorageType;

import java.util.Set;

public interface DependencyManager extends AutoCloseable {
    void loadDependencies(Set<Dependency> dependencies);

    void loadStorageDependencies(Set<StorageType> types);

    ClassLoader obtainClassLoaderWith(Set<Dependency> dependencies);

    @Override
    void close();
}
