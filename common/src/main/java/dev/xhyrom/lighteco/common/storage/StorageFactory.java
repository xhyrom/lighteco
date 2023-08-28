package dev.xhyrom.lighteco.common.storage;

import com.google.common.collect.ImmutableSet;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.memory.MemoryStorageProvider;
import dev.xhyrom.lighteco.common.storage.provider.sql.SqlStorageProvider;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.file.H2ConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.file.SqliteConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari.MariaDBConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari.MySQLConnectionFactory;

import java.util.Set;

public class StorageFactory {
    private final LightEcoPlugin plugin;

    public StorageFactory(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public Set<StorageType> getRequiredTypes() {
        return ImmutableSet.of(StorageType.parse(this.plugin.getConfig().storage.provider));
    }

    public Storage get() {
        // todo: use config
        String provider = this.plugin.getConfig().storage.provider;
        Storage storage = new Storage(this.plugin, createProvider(provider));

        storage.init();

        return storage;
    }

    private StorageProvider createProvider(String provider) {
        switch (provider.toLowerCase()) {
            case "memory":
                return new MemoryStorageProvider(this.plugin);
            case "h2":
                return new SqlStorageProvider(
                        this.plugin,
                        new H2ConnectionFactory(this.plugin.getBootstrap().getDataDirectory().resolve("lighteco-h2").toAbsolutePath())
                );
            case "sqlite":
                return new SqlStorageProvider(
                        this.plugin,
                        new SqliteConnectionFactory(this.plugin.getBootstrap().getDataDirectory().resolve("lighteco-sqlite.db"))
                );
            case "mysql":
                return new SqlStorageProvider(
                        this.plugin,
                        new MySQLConnectionFactory(this.plugin.getConfig().storage.data)
                );
            case "mariadb":
                return new SqlStorageProvider(
                        this.plugin,
                        new MariaDBConnectionFactory(this.plugin.getConfig().storage.data)
                );
            default:
                throw new IllegalArgumentException("Unknown storage provider: " + provider);
        }
    }
}
