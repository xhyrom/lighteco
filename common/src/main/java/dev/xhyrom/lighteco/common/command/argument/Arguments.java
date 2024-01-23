package dev.xhyrom.lighteco.common.command.argument;

import dev.xhyrom.lighteco.common.model.user.User;

import java.util.Map;

public class Arguments {
    private final Map<String, ?> arguments;

    public Arguments(Map<String, ?> arguments) {
        this.arguments = arguments;
    }

    public User offlineUser(String name) {
        return (User) this.arguments.get(name);
    }

    public int integer(String name) {
        return (int) this.arguments.get(name);
    }

    public double dbl(String name) {
        return (double) this.arguments.get(name);
    }
}
