package dev.xhyrom.lighteco.common.command;

import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.command.argument.Arguments;
import dev.xhyrom.lighteco.common.commands.BalanceCommand;
import dev.xhyrom.lighteco.common.commands.BalanceOtherCommand;
import dev.xhyrom.lighteco.common.commands.CurrencyParentCommand;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    protected final LightEcoPlugin plugin;
    private final List<Command> commands = new ArrayList<>();

    public CommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Currency currency) {
        register(currency, false);
    }

    public void register(Currency currency, boolean main) {
        commands.add(new CurrencyParentCommand(currency));
        if (main) {
            commands.add(BalanceCommand.create(currency));
            commands.add(BalanceOtherCommand.create(currency));
        }
    }

    public void execute(CommandSender sender, String name, String[] args) {
        Command command = this.commands.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(name) && cmd.getArgs().size() == args.length)
                .findFirst()
                .orElse(null);

        if (command == null) {
            sender.sendMessage(Component.text("Command not found.")); // TODO: change
            return;
        }

        command.execute(this.plugin, sender, new Arguments(plugin, command, List.of(args)));
    }
}
