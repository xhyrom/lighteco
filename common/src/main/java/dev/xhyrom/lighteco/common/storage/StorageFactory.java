package dev.xhyrom.lighteco.common.storage;

import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.memory.MemoryStorageProvider;
import dev.xhyrom.lighteco.common.storage.provider.sql.SqlStorageProvider;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.file.H2ConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.file.SqliteConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari.MariaDBConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari.MySQLConnectionFactory;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari.PostgreSQLConnectionFactory;

import java.util.Set;

public class StorageFactory {
    private final LightEcoPlugin plugin;

    public StorageFactory(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public Set<StorageType> getRequiredTypes() {
        return Set.of(this.plugin.getConfig().storage.provider);
    }

    public Storage get() {
        StorageType provider = this.plugin.getConfig().storage.provider;
        Storage storage = new Storage(this.plugin, createProvider(provider));

        storage.init();

        return storage;
    }

    private StorageProvider createProvider(StorageType type) {
        return switch (type) {
            case MEMORY -> new MemoryStorageProvider(this.plugin);
            case H2 -> new SqlStorageProvider(
                    this.plugin,
                    new H2ConnectionFactory(this.plugin
                            .getBootstrap()
                            .getDataDirectory()
                            .resolve("lighteco-h2")
                            .toAbsolutePath()));
            case SQLITE -> new SqlStorageProvider(
                    this.plugin,
                    new SqliteConnectionFactory(this.plugin
                            .getBootstrap()
                            .getDataDirectory()
                            .resolve("lighteco-sqlite.db")));
            case MYSQL -> new SqlStorageProvider(
                    this.plugin, new MySQLConnectionFactory(this.plugin.getConfig().storage.data));
            case MARIADB -> new SqlStorageProvider(
                    this.plugin,
                    new MariaDBConnectionFactory(this.plugin.getConfig().storage.data));
            case POSTGRESQL -> new SqlStorageProvider(
                    this.plugin,
                    new PostgreSQLConnectionFactory(this.plugin.getConfig().storage.data));
            default -> throw new IllegalArgumentException(
                    "Unknown storage provider: " + type.name());
        };
    }
}
