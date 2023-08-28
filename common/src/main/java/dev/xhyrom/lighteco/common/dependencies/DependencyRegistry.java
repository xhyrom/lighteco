package dev.xhyrom.lighteco.common.dependencies;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import dev.xhyrom.lighteco.common.storage.StorageType;

import java.util.LinkedHashSet;
import java.util.Set;

public class DependencyRegistry {
    private static final SetMultimap<StorageType, Dependency> STORAGE_DEPENDENCIES = ImmutableSetMultimap.<StorageType, Dependency>builder()
            .putAll(StorageType.SQLITE, Dependency.SQLITE_DRIVER)
            .putAll(StorageType.H2, Dependency.H2_DRIVER)
            .build();

    public Set<Dependency> resolveStorageDependencies(Set<StorageType> types) {
        Set<Dependency> dependencies = new LinkedHashSet<>();

        for (StorageType type : types) {
            dependencies.addAll(STORAGE_DEPENDENCIES.get(type));
        }

        return dependencies;
    }
}
