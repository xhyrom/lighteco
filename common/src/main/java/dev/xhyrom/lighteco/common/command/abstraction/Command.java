package dev.xhyrom.lighteco.common.command.abstraction;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.config.message.CurrencyMessageConfig;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public abstract class Command {
    @NonNull
    protected final String name;

    @NonNull
    private final String description;

    @NonNull
    private final List<String> aliases = new ArrayList<>();

    public Command(@NonNull String name, @NonNull String description, String... aliases) {
        this.name = name;
        this.description = description;

        Collections.addAll(this.aliases, aliases);
    }

    public abstract CommandNode<CommandSource> build();

    protected LiteralArgumentBuilder<CommandSource> builder() {
        return LiteralArgumentBuilder.literal(name);
    }

    protected CurrencyMessageConfig getCurrencyMessageConfig(LightEcoPlugin plugin, Currency currency) {
        Map<String, CurrencyMessageConfig> config = plugin.getConfig().messages.currency;
        CurrencyMessageConfig currencyMessageConfig = config.get(currency.getIdentifier());

        if (currencyMessageConfig == null) {
            return config.get("default");
        }

        return currencyMessageConfig;
    }
}
