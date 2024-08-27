package dev.xhyrom.lighteco.common.command.abstraction;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import dev.xhyrom.lighteco.common.command.CommandSource;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class Command {
    @NonNull protected final String name;

    @NonNull private final String description;

    @NonNull private final List<String> aliases = new ArrayList<>();

    public Command(@NonNull String name, @NonNull String description, String... aliases) {
        this.name = name;
        this.description = description;

        Collections.addAll(this.aliases, aliases);
    }

    public abstract CommandNode<CommandSource> build();

    protected LiteralArgumentBuilder<CommandSource> builder() {
        return LiteralArgumentBuilder.literal(name);
    }
}
