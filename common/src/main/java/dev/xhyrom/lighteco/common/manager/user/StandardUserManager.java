package dev.xhyrom.lighteco.common.manager.user;

import dev.xhyrom.lighteco.common.manager.ConcurrentManager;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StandardUserManager extends ConcurrentManager<UUID, User> implements UserManager {
    private final LightEcoPlugin plugin;

    public StandardUserManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public User apply(UUID uniqueId) {
        return new User(this.plugin, uniqueId);
    }

    @Override
    public CompletableFuture<User> loadUser(UUID uniqueId) {
        return loadUser(uniqueId, null);
    }

    @Override
    public CompletableFuture<User> loadUser(UUID uniqueId, String username) {
        return this.plugin.getStorage().loadUser(uniqueId, username);
    }

    @Override
    public CompletableFuture<Void> saveUser(User user) {
       return this.plugin.getStorage().saveUser(user.getProxy());
    }

    @Override
    public CompletableFuture<Void> saveUsers(User... users) {
        return this.plugin.getStorage().saveUsers(
                Arrays.stream(users)
                        .map(User::getProxy)
                        .toArray(dev.xhyrom.lighteco.api.model.user.User[]::new)
        );
    }
}