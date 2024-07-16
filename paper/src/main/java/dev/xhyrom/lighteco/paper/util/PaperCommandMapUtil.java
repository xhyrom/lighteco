package dev.xhyrom.lighteco.paper.util;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

public class PaperCommandMapUtil {
    private static final Constructor<PluginCommand> COMMAND_CONSTRUCTOR;

    static {
        try {
            COMMAND_CONSTRUCTOR =
                    PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            COMMAND_CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final LightEcoPlugin plugin;

    public PaperCommandMapUtil(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(CommandExecutor executor, TabCompleter completer, List<String> aliases) {
        Plugin bukkitPlugin = (JavaPlugin) this.plugin.getBootstrap().getLoader();
        CommandMap commandMap = bukkitPlugin.getServer().getCommandMap();
        Map<String, Command> knownCommands = commandMap.getKnownCommands();

        for (String name : aliases) {
            if (!name.toLowerCase().equals(name)) {
                throw new IllegalArgumentException(
                        "Command aliases must be lowercase! (name: " + name + ")");
            }

            try {
                PluginCommand command = COMMAND_CONSTRUCTOR.newInstance(name, bukkitPlugin);

                commandMap.register(bukkitPlugin.getName().toLowerCase(), command);
                knownCommands.put(
                        bukkitPlugin.getName().toLowerCase() + ":" + name.toLowerCase(), command);
                knownCommands.put(name, command);

                command.setLabel(name);
                command.setExecutor(executor);
                command.setTabCompleter(completer);

                commandMap.register(bukkitPlugin.getName(), command);
            } catch (ReflectiveOperationException e) {
                this.plugin.getBootstrap().getLogger().error("Failed to register command: %s", e);
            }
        }
    }
}
