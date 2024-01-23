package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.command.argument.ArgumentType;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

public class IntegerArgument extends Argument<Integer> {
    protected IntegerArgument(String name) {
        super(name);
    }

    @Override
    public Class<Integer> getPrimitiveType() {
        return Integer.class;
    }

    @Override
    public ArgumentType getArgumentType() {
        return ArgumentType.INTEGER;
    }

    @Override
    public Integer parse(LightEcoPlugin plugin, String input) {
        return Integer.parseInt(input);
    }
}
