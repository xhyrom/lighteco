package dev.xhyrom.lighteco.common.storage;

import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.memory.MemoryStorageProvider;
import dev.xhyrom.lighteco.common.storage.provider.sql.SqlStorageProvider;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.file.SqliteConnectionFactory;

public class StorageFactory {
    private final LightEcoPlugin plugin;

    public StorageFactory(LightEcoPlugin plugin) {
        this.plugin = plugin;
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
            case "sqlite":
                return new SqlStorageProvider(
                        this.plugin,
                        new SqliteConnectionFactory(this.plugin.getBootstrap().getDataFolder().toPath().resolve("data.db"))
                );
            default:
                throw new IllegalArgumentException("Unknown storage provider: " + provider);
        }
    }
}
