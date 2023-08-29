package dev.xhyrom.lighteco.api.manager;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface ContextManager<T> {
    /**
     * Gets the unique id of a player.
     *
     * @param context the context to get the unique id of
     * @return the unique id of the player
     */
    @NonNull UUID getPlayerUniqueId(@NonNull T context);

    /**
     * Gets the player class of the platform.
     *
     * @return the player class
     */
    @NonNull Class<?> getPlayerClass();
}
