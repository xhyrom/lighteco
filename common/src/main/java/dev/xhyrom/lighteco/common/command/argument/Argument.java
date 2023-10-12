package dev.xhyrom.lighteco.common.command.argument;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;

public abstract class Argument<T> {
    @Getter
    private final String name;
    protected final LightEcoPlugin plugin;

    protected Argument(LightEcoPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract Class<T> getPrimitiveType();
    public abstract ArgumentType getArgumentType();

    public abstract T parse(String input);
}
