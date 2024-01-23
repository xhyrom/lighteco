package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.command.argument.ArgumentType;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

public class DoubleArgument extends Argument<Double> {
    protected DoubleArgument(String name) {
        super(name);
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
    public Double parse(LightEcoPlugin plugin, String input) {
        return Double.parseDouble(input);
    }
}
