package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.command.argument.ArgumentType;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;

public class StringArgument extends Argument<String> {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public ArgumentType getArgumentType() {
        return ArgumentType.STRING;
    }

    @Override
    public String parse(LightEcoPlugin plugin, String input) {
        return input;
    }
}
