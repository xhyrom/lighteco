package dev.xhyrom.lighteco.common.command.argument;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;

public abstract class Argument<T> {
    @Getter
    private final String name;

    protected Argument(String name) {
        this.name = name;
    }

    public abstract Class<T> getPrimitiveType();
    public abstract ArgumentType getArgumentType();

    public abstract T parse(LightEcoPlugin plugin, String input);
}
