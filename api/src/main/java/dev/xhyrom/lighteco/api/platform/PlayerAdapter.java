package dev.xhyrom.lighteco.api.platform;

import dev.xhyrom.lighteco.api.model.user.User;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public interface PlayerAdapter<T> {
    /**
     * Gets the user of a player.
     *
     * @param player the player to get the user of
     * @return the user
     */
    @NonNull User getUser(@NonNull T player);

    /**
     * Loads the user of a player.
     *
     * @param player the player to load the user of
     * @return a future that completes with the user
     */
    @NonNull CompletableFuture<User> loadUser(@NonNull T player);
}
