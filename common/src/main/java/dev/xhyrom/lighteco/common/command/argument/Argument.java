package dev.xhyrom.lighteco.common.command.argument;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public class Argument {
    @Getter
    private final String name;
    @Getter
    private final boolean required;
    @Getter
    private final Component description;

    public Argument(String name, boolean required, Component description) {
        this.name = name;
        this.required = required;
        this.description = description;
    }
}
