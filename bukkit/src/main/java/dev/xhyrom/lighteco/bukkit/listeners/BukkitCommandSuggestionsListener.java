package dev.xhyrom.lighteco.bukkit.listeners;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import dev.xhyrom.lighteco.bukkit.brigadier.BukkitBrigadier;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.common.command.CommandSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.Map;

public class BukkitCommandSuggestionsListener implements Listener {
    private final BukkitLightEcoPlugin plugin;

    public BukkitCommandSuggestionsListener(BukkitLightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        BukkitCommandSender sender = new BukkitCommandSender(event.getPlayer(), this.plugin.getCommandManager().audienceFactory);
        CommandSource source = new CommandSource(this.plugin, sender);
        if (event.isAsynchronous() || !event.hasFiredAsync()) {
            for (CommandNode<CommandSource> command : this.plugin.getCommandManager().getDispatcher().getRoot().getChildren()) {
                if (!command.canUse(source)) continue;

                System.out.println("Adding command " + command.getName() + " to suggestions");

                BukkitBrigadier.removeChild(event.getCommandNode(), command.getName());
                event.getCommandNode().addChild((CommandNode) command);
            }
        }
    }
}
