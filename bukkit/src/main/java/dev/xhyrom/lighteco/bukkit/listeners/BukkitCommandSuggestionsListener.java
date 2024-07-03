package dev.xhyrom.lighteco.bukkit.listeners;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import dev.xhyrom.lighteco.bukkit.brigadier.BukkitBrigadier;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.common.command.CommandSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BukkitCommandSuggestionsListener implements Listener {
    private final BukkitLightEcoPlugin plugin;

    public BukkitCommandSuggestionsListener(BukkitLightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        BukkitCommandSender sender = new BukkitCommandSender(event.getPlayer(), this.plugin.getCommandManager().audienceFactory);
        CommandSource source = new CommandSource(this.plugin, sender);
        if (event.isAsynchronous() || !event.hasFiredAsync()) {
            for (CommandNode<CommandSource> command : this.plugin.getCommandManager().getDispatcher().getRoot().getChildren()) {
                BukkitBrigadier.removeChild(event.getCommandNode(), command.getName());
                BukkitBrigadier.removeChild(event.getCommandNode(), command.getName() + ":" + command.getName());

                if (!command.canUse(source)) continue;

                CommandNode<CommandSource> clone = new LiteralCommandNode(command.getName(), command.getCommand(), command.getRequirement(), command.getRedirect(), command.getRedirectModifier(), command.isFork());

                for (CommandNode<CommandSource> child : command.getChildren()) {
                    if (child.canUse(source))
                        clone.addChild(child);
                }

                event.getCommandNode().addChild((CommandNode) clone);
            }
        }
    }
}
