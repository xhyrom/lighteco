package dev.xhyrom.lighteco.common.model.chat;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface CommandSender {
    String getUsername();
    UUID getUniqueId();

    boolean eligible(String permission);
    void sendMessage(Component message);
}
