package dev.xhyrom.lighteco.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xhyrom.lighteco.common.commands.BalanceCommand;
import dev.xhyrom.lighteco.common.commands.CurrencyParentCommand;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;

public class CommandManager {
    protected final LightEcoPlugin plugin;
    @Getter
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public CommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Currency currency) {
        register(currency, false);
    }

    public void register(Currency currency, boolean main) {
        dispatcher.getRoot().addChild(new CurrencyParentCommand(currency).build());
        if (main) {
            dispatcher.getRoot().addChild(BalanceCommand.create(currency).build());
        }
    }

    public void execute(CommandSender sender, String name, String[] args) {
        final CommandSource source = new CommandSource(this.plugin, sender);
        final ParseResults<CommandSource> parseResults = dispatcher.parse(name + " " + String.join(" ", args), source);

        try {
            dispatcher.execute(parseResults);
        } catch (CommandSyntaxException e) {
            sender.sendMessage(Component.text(e.getMessage()));
        }
    }
}
