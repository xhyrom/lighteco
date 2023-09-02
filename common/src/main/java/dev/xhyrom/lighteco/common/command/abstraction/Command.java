package dev.xhyrom.lighteco.common.command.abstraction;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public abstract class Command {
    @Getter
    @NonNull
    private final String name;

    @Getter
    @Nullable
    private final String permission;

    @Getter
    @NonNull
    private final List<Argument> args;

    public Command(@NonNull String name, @Nullable String permission, @NonNull Argument... args) {
        this.name = name;
        this.permission = permission;
        this.args = List.of(args);
    }
}
