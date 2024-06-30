package dev.xhyrom.lighteco.common.command.argument.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OfflineUserArgument implements ArgumentType<String> {
    public static OfflineUserArgument offlineUserArgument() {
        return new OfflineUserArgument();
    }

    public static User get(CommandContext<CommandSource> context, String name) {
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

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        LightEcoPlugin plugin = ((CommandSource) context.getSource()).plugin();

        String remaining = builder.getRemaining();

        for (UUID uniqueId : plugin.getBootstrap().getOnlinePlayers()) {
            User user = plugin.getUserManager().getIfLoaded(uniqueId);
            if (user == null) continue;

            builder.suggest(user.getUsername());
        }

        return builder.buildFuture();
    }
}
