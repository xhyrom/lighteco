package dev.xhyrom.lighteco.common.manager.user;

import dev.xhyrom.lighteco.common.manager.Manager;
import dev.xhyrom.lighteco.common.model.user.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager extends Manager<UUID, User> {
    CompletableFuture<Void> saveUser(User user);

    CompletableFuture<Void> load();
    CompletableFuture<User> loadUser(UUID uniqueId);

    void invalidateCaches();
}
