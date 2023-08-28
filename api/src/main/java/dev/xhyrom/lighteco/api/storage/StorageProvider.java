package dev.xhyrom.lighteco.api.storage;

import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface StorageProvider {
    @NonNull User loadUser(@NonNull UUID uniqueId) throws Exception;
    // todo: look at this
    void saveUser(@NonNull User user);
}
