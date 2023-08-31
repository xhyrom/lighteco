package dev.xhyrom.lighteco.common.storage.provider.memory;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class MemoryStorageProvider implements StorageProvider {
    private HashMap<UUID, User> userDatabase;

    private final LightEcoPlugin plugin;
    public MemoryStorageProvider(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        userDatabase = new HashMap<>();
    }

    @Override
    public void shutdown() {
        userDatabase = null;
    }

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId, @Nullable String username) {
        this.simulateSlowDatabaseQuery();

        return this.createUser(uniqueId, username, userDatabase.get(uniqueId));
    }

    @Override
    public void saveUser(@NonNull User user) {
        this.simulateSlowDatabaseQuery();

        this.userDatabase.put(user.getUniqueId(), user);
    }

    @Override
    public void saveUsers(@NotNull @NonNull User... users) {
        for (User user : users) {
            this.userDatabase.put(user.getUniqueId(), user);
        }
    }

    private User createUser(UUID uniqueId, String username, User data) {
        dev.xhyrom.lighteco.common.model.user.User user = this.plugin.getUserManager().getOrMake(uniqueId);
        if (username != null)
            user.setUsername(username);

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
