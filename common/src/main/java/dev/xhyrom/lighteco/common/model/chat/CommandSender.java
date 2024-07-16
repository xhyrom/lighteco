package dev.xhyrom.lighteco.common.model.chat;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public interface CommandSender {
    String getUsername();
    @Nullable UUID getUniqueId();

    boolean eligible(String permission);
    void sendMessage(Component message);

    default boolean isConsole() {
        return getUniqueId() == null;
    }
}
