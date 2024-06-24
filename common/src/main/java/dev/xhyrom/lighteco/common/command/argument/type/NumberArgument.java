package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

abstract class NumberArgument<T> extends Argument<T> {
    private final T min;
    private final T max;

    protected NumberArgument(String name, T min, T max) {
        super(name);

        this.min = min;
        this.max = max;
    }

    public abstract T parse(String input);

    @Override
    public T parse(LightEcoPlugin plugin, String input) {
        T value = parse(input);
        if (value == null || (min != null && compare(value, min) < 0) || (max != null && compare(value, max) > 0)) {
            return null;
        }

        return value;
    }

    private int compare(T a, T b) {
        return ((Comparable<T>) a).compareTo(b);
    }
}
