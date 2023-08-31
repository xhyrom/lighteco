package dev.xhyrom.lighteco.common.manager.user;

import dev.xhyrom.lighteco.common.manager.Manager;
import dev.xhyrom.lighteco.common.model.user.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager extends Manager<UUID, User> {
    UserHousekeeper getHousekeeper();

    CompletableFuture<Void> saveUser(User user);
    CompletableFuture<Void> saveUsers(User... users);

    CompletableFuture<User> loadUser(UUID uniqueId);
    CompletableFuture<User> loadUser(UUID uniqueId, String username);
}
