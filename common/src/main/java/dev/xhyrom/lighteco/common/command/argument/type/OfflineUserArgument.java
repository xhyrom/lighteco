package dev.xhyrom.lighteco.common.command.argument.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;

public class OfflineUserArgument implements ArgumentType<String> {
    private OfflineUserArgument() {}

    public static User getOfflineUser(CommandContext<CommandSource> context, String name) {
        String userName = context.getArgument(name, String.class);
        LightEcoPlugin plugin = context.getSource().plugin();

        UUID uniqueId = plugin.getBootstrap().lookupUniqueId(userName).orElse(null);
        if (uniqueId == null) {
            return null;
        }

        return plugin.getUserManager().loadUser(uniqueId).join();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }
}