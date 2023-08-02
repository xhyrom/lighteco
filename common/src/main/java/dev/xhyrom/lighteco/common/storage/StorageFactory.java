package dev.xhyrom.lighteco.common.storage;

import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.memory.MemoryStorageProvider;

public class StorageFactory {
    private final LightEcoPlugin plugin;

    public StorageFactory(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public Storage get() {
        // todo: use config
        String provider = "memory";//this.plugin.getConfig().getString("storage.provider");

        return new Storage(this.plugin, createProvider(provider));
    }

    private StorageProvider createProvider(String provider) {
        switch (provider.toLowerCase()) {
            case "memory":
                return new MemoryStorageProvider(this.plugin);
            default:
                throw new IllegalArgumentException("Unknown storage provider: " + provider);
        }
    }
}
