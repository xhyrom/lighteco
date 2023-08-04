package dev.xhyrom.lighteco.common.manager.user;

import dev.xhyrom.lighteco.common.manager.AbstractManager;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StandardUserManager extends AbstractManager<UUID, User> implements UserManager {
    private final LightEcoPlugin plugin;

    public StandardUserManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public User apply(UUID uniqueId) {
        return new User(this.plugin, uniqueId);
    }

    @Override
    public CompletableFuture<Void> load() {
        Set<UUID> uniqueIds = new HashSet<>(keys());
        uniqueIds.addAll(this.plugin.getBootstrap().getOnlinePlayers());

        return uniqueIds.stream()
                .map(id -> this.plugin.getStorage().loadUser(id))
                .collect(CompletableFuture::allOf, (future, userFuture) -> future.join(), (future, userFuture) -> future.join());
    }

    @Override
    public void invalidateCaches() {
        values().forEach(User::invalidateCaches);
    }
}