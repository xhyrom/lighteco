package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.ArgumentType;

public class DoubleArgument extends NumberArgument<Double> {
    protected DoubleArgument(String name, Double min, Double max) {
        super(name, min, max);
    }

    @Override
    public Class<Double> getPrimitiveType() {
        return Double.class;
    }

    @Override
    public ArgumentType getArgumentType() {
        return ArgumentType.DOUBLE;
    }

    @Override
    public Double parse(String input) {
        return Double.parseDouble(input);
    }
}
