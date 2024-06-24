package dev.xhyrom.lighteco.common.command.argument;

import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arguments {
    private final LightEcoPlugin plugin;
    private final Command command;
    private final List<String> arguments;
    private final Map<String, Object> mappedArguments = new HashMap<>();

    public Arguments(LightEcoPlugin plugin, Command command, List<String> arguments) {
        this.plugin = plugin;
        this.command = command;
        this.arguments = arguments;

        for (int i = 0; i < arguments.size(); i++) {
            String arg = arguments.get(i);
            this.mappedArguments.put(command.getArgs().get(i).getName(), (Object) command.getArgs().get(i).parse(plugin, arg));
        }
    }

    public String string(String name) {
        return (String) this.mappedArguments.get(name);
    }

    public User offlineUser(String name) {
        return (User) this.mappedArguments.get(name);
    }

    public int integer(String name) {
        return (int) this.mappedArguments.get(name);
    }

    public double dbl(String name) {
        return (double) this.mappedArguments.get(name);
    }

    public String raw(int index) {
        return (String) this.arguments.get(index);
    }

    public int size() {
        return this.arguments.size();
    }

    public Arguments subList(int fromIndex) {
        return new Arguments(this.plugin, this.command, this.arguments.subList(fromIndex, this.arguments.size()));
    }
}
