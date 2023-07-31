package dev.xhyrom.lighteco.api.platform;

import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public interface PlayerAdapter<T> {
    @NonNull User getUser(@NonNull T player);

    @NonNull CompletableFuture<User> loadUser(@NonNull T player);
}
