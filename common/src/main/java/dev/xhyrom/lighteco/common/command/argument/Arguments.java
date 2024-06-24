package dev.xhyrom.lighteco.common.command.argument;

import dev.xhyrom.lighteco.common.model.user.User;

import java.util.List;
import java.util.Map;

public class Arguments {
    private final List<String> arguments;
    private final Map<String, ?> mappedArguments;

    public Arguments(List<String> arguments) {
        this.arguments = arguments;

        this.mappedArguments = Map.of();
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

    public Arguments subList(int fromIndex) {
        return new Arguments(this.arguments.subList(fromIndex, this.arguments.size()));
    }
}
