package dev.xhyrom.lighteco.common.model.user;

import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class User implements dev.xhyrom.lighteco.api.model.user.User {
    @Getter
    private final UUID uniqueId;

    public User(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public @NonNull String getUsername() {
        return null;
    }
}
