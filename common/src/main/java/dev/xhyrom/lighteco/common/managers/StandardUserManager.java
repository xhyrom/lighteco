package dev.xhyrom.lighteco.common.managers;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.managers.UserManager;
import dev.xhyrom.lighteco.common.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StandardUserManager implements UserManager {
    private final LightEcoPlugin plugin;

    public StandardUserManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public User apply(UUID uniqueId) {
        return new dev.xhyrom.lighteco.common.model.user.User(uniqueId);
    }
    @Override
    public @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId) {
        return null;
    }

    @Override
    public @NonNull CompletableFuture<Void> saveUser(User user) {
        return null;
    }

    @Override
    public @Nullable User getUser(@NonNull UUID uniqueId) {
        return null;
    }

    @Override
    public boolean isLoaded(@NonNull UUID uniqueId) {
        return false;
    }
}
