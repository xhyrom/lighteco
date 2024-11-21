package dev.xhyrom.lighteco.common.command.suggestion.type;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OfflineUserSuggestionProvider implements SuggestionProvider<CommandSource> {
    public static OfflineUserSuggestionProvider create() {
        return new OfflineUserSuggestionProvider();
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(
            CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        LightEcoPlugin plugin = context.getSource().plugin();

        for (UUID uniqueId : plugin.getBootstrap().getOnlinePlayers()) {
            User user = plugin.getUserManager().getIfLoaded(uniqueId);
            if (user == null || user.getUsername() == null) continue;

            builder.suggest(user.getUsername());
        }

        return builder.buildFuture();
    }
}
