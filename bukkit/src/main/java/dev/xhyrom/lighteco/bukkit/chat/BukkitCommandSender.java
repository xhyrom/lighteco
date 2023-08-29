package dev.xhyrom.lighteco.bukkit.chat;

import dev.xhyrom.lighteco.common.model.chat.AbstractCommandSender;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitCommandSender extends AbstractCommandSender<CommandSender> {
    private final Audience audience;

    public BukkitCommandSender(CommandSender sender, BukkitAudiences audienceFactory) {
        super(sender);

        this.audience = audienceFactory.sender(sender);
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
    public void sendMessage(Component message) {
        this.audience.sendMessage(message);
    }
}