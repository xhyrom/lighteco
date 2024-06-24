package dev.xhyrom.lighteco.common.command.abstraction;

import dev.xhyrom.lighteco.common.command.argument.Arguments;
import dev.xhyrom.lighteco.common.command.argument.type.StringArgument;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;

@Getter
public class ParentCommand extends Command {
    private final Command[] children;

    public ParentCommand(@NonNull String name, @Nullable String permission, @NonNull Command... children) {
        super(name, permission, new StringArgument("child"));

        this.children = children;
    }

    @Override
    public void execute(LightEcoPlugin plugin, CommandSender sender, Arguments args) {
        String childName = args.string("child");
        Command child = Arrays.stream(getChildren())
                .filter(cmd -> cmd.getName().equalsIgnoreCase(childName))
                .findFirst()
                .orElse(null);

        if (child == null) {
            return;
        }

        child.execute(plugin, sender, args.subList(1));
    }
}
