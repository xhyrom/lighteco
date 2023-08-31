package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.manager.UserManager;
import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiUserManager extends ApiAbstractManager<dev.xhyrom.lighteco.common.manager.user.UserManager> implements UserManager {
    public ApiUserManager(LightEcoPlugin plugin, dev.xhyrom.lighteco.common.manager.user.UserManager handler) {
        super(plugin, handler);
    }

    private User wrap(dev.xhyrom.lighteco.common.model.user.User handler) {
        return handler.getProxy();
    }

    @Override
    public @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId) {
        return loadUser(uniqueId, null);
    }

    @Override
    public @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId, String username) {
        return this.plugin.getStorage().loadUser(uniqueId, username)
                .thenApply(this::wrap);
    }

    @Override
    public @NonNull CompletableFuture<Void> saveUser(@NonNull User user) {
        return this.plugin.getStorage().saveUser(user);
    }

    @Override
    public @NonNull CompletableFuture<Void> saveUsers(@NotNull @NonNull User... users) {
        return this.plugin.getStorage().saveUsers(users);
    }

    @Override
    public @Nullable User getUser(@NonNull UUID uniqueId) {
        return wrap(this.handler.getIfLoaded(uniqueId));
    }

    @Override
    public boolean isLoaded(@NonNull UUID uniqueId) {
        return this.handler.isLoaded(uniqueId);
    }
}
