package dev.xhyrom.lighteco.api.platform;

import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public interface PlayerAdapter<T> {
    @NonNull CompletableFuture<User> getUser(@NonNull T player);
}
