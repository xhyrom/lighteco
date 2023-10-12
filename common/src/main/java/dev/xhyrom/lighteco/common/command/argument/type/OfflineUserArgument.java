package dev.xhyrom.lighteco.common.command.argument.type;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.command.argument.ArgumentType;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;

public class OfflineUserArgument extends Argument<User> {
    public OfflineUserArgument(LightEcoPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public Class<User> getPrimitiveType() {
        return User.class;
    }

    @Override
    public ArgumentType getArgumentType() {
        return ArgumentType.OFFLINE_USER;
    }

    @Override
    public User parse(String input) {
        UUID uniqueId = this.plugin.getBootstrap().lookupUniqueId(input).orElse(null);
        if (uniqueId == null) {
            return null;
        }

        return this.plugin.getUserManager().loadUser(uniqueId).join();
    }
}
