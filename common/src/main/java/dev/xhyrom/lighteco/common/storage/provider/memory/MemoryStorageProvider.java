package dev.xhyrom.lighteco.common.storage.provider.memory;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class MemoryStorageProvider implements StorageProvider {
    private final HashMap<UUID, User> userDatabase = new HashMap<>();

    private final LightEcoPlugin plugin;
    public MemoryStorageProvider(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() throws Exception {}

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId) {
        this.simulateSlowDatabaseQuery();

        return this.createUser(uniqueId, userDatabase.get(uniqueId));
    }

    @Override
    public void saveUser(@NonNull User user) {
        this.simulateSlowDatabaseQuery();

        this.userDatabase.put(user.getUniqueId(), user);
    }

    private User createUser(UUID uniqueId, User data) {
        dev.xhyrom.lighteco.common.model.user.User user = this.plugin.getUserManager().getOrMake(uniqueId);

        return user.getProxy();
    }

    private void simulateSlowDatabaseQuery() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
