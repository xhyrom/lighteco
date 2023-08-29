package dev.xhyrom.lighteco.api.storage;

import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public interface StorageProvider {
    void init() throws Exception;
    void shutdown() throws Exception;

    @NonNull User loadUser(@NonNull UUID uniqueId, @Nullable String username) throws Exception;
    void saveUser(@NonNull User user) throws Exception;
}
