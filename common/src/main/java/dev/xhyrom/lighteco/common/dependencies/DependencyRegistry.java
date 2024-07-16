package dev.xhyrom.lighteco.common.dependencies;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import dev.xhyrom.lighteco.common.messaging.MessagingType;
import dev.xhyrom.lighteco.common.storage.StorageType;

import java.util.LinkedHashSet;
import java.util.Set;

public class DependencyRegistry {
    private static final SetMultimap<StorageType, Dependency> STORAGE_DEPENDENCIES =
            ImmutableSetMultimap.<StorageType, Dependency>builder()
                    .putAll(StorageType.SQLITE, Dependency.SQLITE_DRIVER)
                    .putAll(StorageType.H2, Dependency.H2_DRIVER)
                    .putAll(StorageType.MYSQL, Dependency.MYSQL_DRIVER, Dependency.HIKARI)
                    .putAll(StorageType.MARIADB, Dependency.MARIADB_DRIVER, Dependency.HIKARI)
                    .putAll(StorageType.POSTGRESQL, Dependency.POSTGRESQL_DRIVER, Dependency.HIKARI)
                    .build();

    private static final SetMultimap<MessagingType, Dependency> MESSAGING_DEPENDENCIES =
            ImmutableSetMultimap.<MessagingType, Dependency>builder()
                    .putAll(
                            MessagingType.REDIS,
                            Dependency.COMMONS_POOL_2,
                            Dependency.JEDIS,
                            Dependency.SLF4J_API,
                            Dependency.SLF4J_SIMPLE)
                    .build();

    public Set<Dependency> resolveStorageDependencies(Set<StorageType> types) {
        Set<Dependency> dependencies = new LinkedHashSet<>();

        for (StorageType type : types) {
            dependencies.addAll(STORAGE_DEPENDENCIES.get(type));
        }

        return dependencies;
    }

    public Set<Dependency> resolveMessagingDependencies(Set<MessagingType> types) {
        Set<Dependency> dependencies = new LinkedHashSet<>();

        for (MessagingType type : types) {
            dependencies.addAll(MESSAGING_DEPENDENCIES.get(type));
        }

        return dependencies;
    }

    public boolean shouldAutoLoad(Dependency dependency) {
        return switch (dependency) {
            case H2_DRIVER, SQLITE_DRIVER -> false;
            default -> true;
        };
    }
}
