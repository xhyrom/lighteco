package dev.xhyrom.lighteco.common.managers.user;

import dev.xhyrom.lighteco.common.managers.Manager;
import dev.xhyrom.lighteco.common.model.user.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager extends Manager<UUID, User> {
    CompletableFuture<Void> load();

    void invalidateCaches();
}
