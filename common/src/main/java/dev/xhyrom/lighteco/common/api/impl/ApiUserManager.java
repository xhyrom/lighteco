package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.manager.UserManager;
import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiUserManager
        extends ApiAbstractManager<dev.xhyrom.lighteco.common.manager.user.UserManager>
        implements UserManager {
    public ApiUserManager(
            LightEcoPlugin plugin, dev.xhyrom.lighteco.common.manager.user.UserManager handle) {
        super(plugin, handle);
    }

    private User wrap(dev.xhyrom.lighteco.common.model.user.User handle) {
        this.plugin.getUserManager().getHousekeeper().registerUsage(handle.getUniqueId());
        return handle.getProxy();
    }

    @Override
    public @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId) {
        return loadUser(uniqueId, null);
    }

    @Override
    public @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId, String username) {
        return this.plugin.getStorage().loadUser(uniqueId, username).thenApply(this::wrap);
    }

    @Override
    public @NonNull CompletableFuture<Void> saveUser(@NonNull User user) {
        return this.plugin.getStorage().saveUser(user);
    }

    @Override
    public @NonNull CompletableFuture<Void> saveUsers(@NonNull User... users) {
        return this.plugin.getStorage().saveUsers(users);
    }

    @Override
    public @Nullable User getUser(@NonNull UUID uniqueId) {
        return wrap(this.handle.getIfLoaded(uniqueId));
    }

    @Override
    public boolean isLoaded(@NonNull UUID uniqueId) {
        return this.handle.isLoaded(uniqueId);
    }
}
