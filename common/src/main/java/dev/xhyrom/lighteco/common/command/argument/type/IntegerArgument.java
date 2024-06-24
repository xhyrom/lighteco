package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.ArgumentType;

public class IntegerArgument extends NumberArgument<Integer> {
    protected IntegerArgument(String name, Integer min, Integer max) {
        super(name, min, max);
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
    public Integer parse(String input) {
        return Integer.parseInt(input);
    }
}
