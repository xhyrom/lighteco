package dev.xhyrom.lighteco.paper.chat;

import dev.xhyrom.lighteco.common.model.chat.AbstractCommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PaperCommandSender extends AbstractCommandSender<CommandSender> {
    public PaperCommandSender(CommandSender sender) {
        super(sender);
    }

    @Override
    public String getUsername() {
        return this.delegate.getName();
    }

    @Override
    public UUID getUniqueId() {
        if (super.delegate instanceof Player player) {
            return player.getUniqueId();
        }

        return null;
    }

    @Override
    public boolean eligible(String permission) {
        return this.delegate.hasPermission(permission);
    }

    @Override
    public void sendMessage(Component message) {
        this.delegate.sendMessage(message);
    }
}