package dev.xhyrom.lighteco.common.storage.provider.memory;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class MemoryStorageProvider implements StorageProvider {
    private HashMap<UUID, User> users = new HashMap<>();

    @Override
    public @NonNull String[] getIdentifiers() {
        return new String[] {"memory"};
    }

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId) throws Exception {
        return users.get(uniqueId);
    }

    @Override
    public void saveUser(@NonNull User user) throws Exception {
        users.put(user.getUniqueId(), user);
    }
}
