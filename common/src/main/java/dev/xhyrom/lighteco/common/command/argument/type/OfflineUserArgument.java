package dev.xhyrom.lighteco.common.command.argument.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.exception.LockedUserException;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.UUID;

public class OfflineUserArgument implements ArgumentType<String> {
    private OfflineUserArgument() {}

    public static User getOfflineUser(CommandContext<CommandSource> context, String name) throws LockedUserException {
        String userName = context.getArgument(name, String.class);
        LightEcoPlugin plugin = context.getSource().plugin();
        CommandSender sender = context.getSource().sender();

        UUID uniqueId = plugin.getBootstrap().lookupUniqueId(userName).orElse(null);
        if (uniqueId == null) {
            return null;
        }

        if (sender.getUniqueId() != uniqueId && plugin.getCommandManager().getLocks().contains(uniqueId)) {
            throw new LockedUserException(uniqueId);
        }

        // Lock the user to prevent race conditions
        plugin.getCommandManager().lockBySender(context.getSource().sender(), uniqueId);

        return plugin.getUserManager().loadUser(uniqueId).join();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }
}