package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.managers.UserManager;
import dev.xhyrom.lighteco.api.platform.PlayerAdapter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiPlayerAdapter<T> implements PlayerAdapter<T> {
    private final UserManager userManager;
    private final ContextManager<T> contextManager;

    public ApiPlayerAdapter(UserManager userManager, ContextManager<T> contextManager) {
        this.userManager = userManager;
        this.contextManager = contextManager;
    }

    @Override
    public @NonNull CompletableFuture<User> getUser(@NonNull T player) {
        UUID uniqueId = contextManager.getPlayerUniqueId(player);
        boolean loaded = userManager.isLoaded(uniqueId);

        if (loaded) {
            return CompletableFuture.completedFuture(userManager.getUser(uniqueId));
        }

        return userManager.loadUser(uniqueId);
    }
}
