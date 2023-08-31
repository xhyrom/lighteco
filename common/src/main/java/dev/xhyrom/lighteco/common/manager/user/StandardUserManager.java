package dev.xhyrom.lighteco.common.manager.user;

import dev.xhyrom.lighteco.common.manager.ConcurrentManager;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StandardUserManager extends ConcurrentManager<UUID, User> implements UserManager {
    private final LightEcoPlugin plugin;
    @Getter
    private final UserHousekeeper housekeeper;

    public StandardUserManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
        this.housekeeper = new UserHousekeeper(plugin, this, UserHousekeeper.timeoutSettings(
                this.plugin.getConfig().housekeeper.expireAfterWrite,
                this.plugin.getConfig().housekeeper.expireAfterWriteUnit
        ));

        this.plugin.getBootstrap().getScheduler().asyncRepeating(
                this.housekeeper,
                this.plugin.getConfig().housekeeper.runInterval,
                this.plugin.getConfig().housekeeper.runIntervalUnit
        );
    }

    @Override
    public User apply(UUID uniqueId) {
        return new User(this.plugin, uniqueId);
    }

    @Override
    public CompletableFuture<User> loadUser(UUID uniqueId) {
        this.plugin.getUserManager().getHousekeeper().registerUsage(uniqueId);

        return loadUser(uniqueId, null);
    }

    @Override
    public CompletableFuture<User> loadUser(UUID uniqueId, String username) {
        this.plugin.getUserManager().getHousekeeper().registerUsage(uniqueId);

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