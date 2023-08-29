package dev.xhyrom.lighteco.api.manager;

import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager {
    @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId);
    @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId, String username);

    @NonNull CompletableFuture<Void> saveUser(@NonNull User user);

    @Nullable User getUser(@NonNull UUID uniqueId);

    boolean isLoaded(@NonNull UUID uniqueId);
}
