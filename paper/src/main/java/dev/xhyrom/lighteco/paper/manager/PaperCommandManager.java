package dev.xhyrom.lighteco.paper.manager;

import com.mojang.brigadier.ParseResults;

import dev.xhyrom.lighteco.common.command.CommandManager;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.paper.chat.PaperCommandSender;
import dev.xhyrom.lighteco.paper.util.PaperCommandMapUtil;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PaperCommandManager extends CommandManager {
    private final PaperCommandMapUtil commandMapUtil;

    public PaperCommandManager(LightEcoPlugin plugin) {
        super(plugin);

        this.commandMapUtil = new PaperCommandMapUtil(plugin);
    }

    @Override
    protected void register(Command command) {
        super.register(command);

        CommandExecutor executor = (sender, bukkitCommand, s, args) -> {
            if (s.startsWith("lighteco:")) {
                s = s.substring(9);
            }

            execute(new PaperCommandSender(sender), s, args);
            return true;
        };

        TabCompleter completer = (sender, bukkitCommand, s, args) -> {
            final List<String> suggestions = new ArrayList<>();
            final CommandSource source = new CommandSource(plugin, new PaperCommandSender(sender));

            final ParseResults<CommandSource> parseResults = getDispatcher()
                    .parse(
                            command.getName()
                                    + (args.length > 0 ? " " + String.join(" ", args) : ""),
                            source);

            getDispatcher()
                    .getCompletionSuggestions(parseResults)
                    .join()
                    .getList()
                    .forEach(suggestion -> suggestions.add(suggestion.getText()));

            return suggestions;
        };

        List<String> aliases = new ArrayList<>(command.getAliases());
        aliases.add(command.getName());

        this.commandMapUtil.register(executor, completer, aliases);
    }
}
