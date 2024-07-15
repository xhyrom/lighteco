package dev.xhyrom.lighteco.common.command;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.commands.BalanceCommand;
import dev.xhyrom.lighteco.common.commands.CurrencyParentCommand;
import dev.xhyrom.lighteco.common.commands.InfoCommand;
import dev.xhyrom.lighteco.common.commands.PayCommand;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;
import java.util.concurrent.*;

public class CommandManager {
    protected final LightEcoPlugin plugin;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("lighteco-command-executor")
            .build()
    );

    @Getter
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    @Getter
    private final Set<UUID> locks = ConcurrentHashMap.newKeySet();
    private final Map<UUID, UUID> locksMappings = new ConcurrentHashMap<>();

    public CommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    // Register built-in commands
    public void register() {
        register(new InfoCommand());
    }

    public void register(Currency currency, boolean main) {
        register(new CurrencyParentCommand(currency));

        if (main) {
            register(BalanceCommand.create(currency));
            register(PayCommand.create(currency));
        }
    }

    protected void register(Command command) {
        dispatcher.getRoot().addChild(command.build());
    }

    public void execute(CommandSender sender, String name, String[] args) {
        if (locks.contains(sender.getUniqueId())) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    this.plugin.getConfig().messages.wait
            ));
            return;
        }

        final CommandSource source = new CommandSource(this.plugin, sender);
        final ParseResults<CommandSource> parseResults = dispatcher.parse(
                name + (args.length > 0 ? " " + String.join(" ", args) : ""),
                source
        );

        locks.add(sender.getUniqueId());

        CompletableFuture.runAsync(() -> {
            try {
                dispatcher.execute(parseResults);
            } catch (CommandSyntaxException e) {
                this.sendError(sender, name, e);
            }  finally {
                this.plugin.getBootstrap().getLogger().debug("Removing lock for " + sender.getUsername());

                UUID target = locksMappings.get(sender.getUniqueId());
                if (target != null) {
                    locks.remove(target);
                    locksMappings.remove(sender.getUniqueId());

                    this.plugin.getBootstrap().getLogger().debug("Removing lock caused by " + sender.getUsername() + " for " + target);
                }

                locks.remove(sender.getUniqueId());
            }
        }, executor);
    }

    public void lockBySender(CommandSender sender, UUID target) {
        locks.add(target);
        locksMappings.put(sender.getUniqueId(), target);
    }

    private void sendError(CommandSender sender, String name, CommandSyntaxException e) {
        sender.sendMessage(Component.text(e.getRawMessage().getString(), NamedTextColor.RED));

        if (e.getInput() != null && e.getCursor() >= 0) {
            int j = Math.min(e.getInput().length(), e.getCursor());

            Component msg = Component.empty().color(NamedTextColor.GRAY).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + name));

            if (j > 10) {
                msg = msg.append(Component.text("..."));
            }

            msg = msg.append(Component.text(e.getInput().substring(Math.max(0, j - 10), j)));

            if (j < e.getInput().length()) {
                Component component = Component.text(e.getInput().substring(j)).color(NamedTextColor.RED).decorate(TextDecoration.UNDERLINED);
                msg = msg.append(component);
            }

            msg = msg.append(Component.translatable("command.context.here").color(NamedTextColor.RED).decorate(TextDecoration.ITALIC));
            sender.sendMessage(msg);
        }
    }
}
