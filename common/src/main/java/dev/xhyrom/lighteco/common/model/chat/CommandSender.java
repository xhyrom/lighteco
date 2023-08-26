package dev.xhyrom.lighteco.common.model.chat;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface CommandSender {
    String getUsername();
    UUID getUniqueId();

    void sendMessage(Component message);
}
