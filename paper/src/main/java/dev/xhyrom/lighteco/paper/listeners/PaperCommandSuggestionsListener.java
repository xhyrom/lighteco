package dev.xhyrom.lighteco.paper.listeners;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.paper.PaperLightEcoPlugin;
import dev.xhyrom.lighteco.paper.chat.PaperCommandSender;
import dev.xhyrom.lighteco.paper.util.PaperBrigadier;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperCommandSuggestionsListener implements Listener {
    private final PaperLightEcoPlugin plugin;

    public PaperCommandSuggestionsListener(PaperLightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        PaperCommandSender sender = new PaperCommandSender(event.getPlayer());
        CommandSource source = new CommandSource(this.plugin, sender);
        if (event.isAsynchronous() || !event.hasFiredAsync()) {
            for (CommandNode<CommandSource> command :
                    this.plugin.getCommandManager().getDispatcher().getRoot().getChildren()) {
                PaperBrigadier.removeChild(event.getCommandNode(), command.getName());
                PaperBrigadier.removeChild(
                        event.getCommandNode(),
                        this.plugin.getBootstrap().getLoader().getName().toLowerCase() + ":"
                                + command.getName());

                if (!command.canUse(source)) continue;

                addChild(event, source, command, createClone(command));
                addChild(
                        event,
                        source,
                        command,
                        createClone(
                                this.plugin.getBootstrap().getLoader().getName().toLowerCase() + ":"
                                        + command.getName(),
                                command));
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
    private void addChild(
            AsyncPlayerSendCommandsEvent<?> event,
            CommandSource source,
            CommandNode<CommandSource> command,
            CommandNode<CommandSource> clone) {
        for (CommandNode<CommandSource> child : command.getChildren()) {
            if (child.canUse(source)) clone.addChild(child);
        }

        event.getCommandNode().addChild((CommandNode) clone);
    }

    private CommandNode<CommandSource> createClone(CommandNode<CommandSource> command) {
        return createClone(command.getName(), command);
    }

    private CommandNode<CommandSource> createClone(
            String name, CommandNode<CommandSource> command) {
        return new LiteralCommandNode<>(
                name,
                command.getCommand(),
                command.getRequirement(),
                command.getRedirect(),
                command.getRedirectModifier(),
                command.isFork());
    }
}
