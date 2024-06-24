package dev.xhyrom.lighteco.common.command.abstraction;

import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.command.argument.Arguments;
import dev.xhyrom.lighteco.common.config.message.CurrencyMessageConfig;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;

@Getter
public abstract class Command {
    @NonNull
    private final String name;

    @Nullable
    private final String permission;

    @NonNull
    private final List<Argument<?>> args;

    public Command(@NonNull String name, @Nullable String permission, @NonNull Argument<?>... args) {
        this.name = name;
        this.permission = permission;
        this.args = List.of(args);
    }

    public abstract void execute(LightEcoPlugin plugin, CommandSender sender, Arguments args);

    protected CurrencyMessageConfig getCurrencyMessageConfig(LightEcoPlugin plugin, Currency currency) {
        Map<String, CurrencyMessageConfig> config = plugin.getConfig().messages.currency;
        CurrencyMessageConfig currencyMessageConfig = config.get(currency.getIdentifier());

        if (currencyMessageConfig == null) {
            return config.get("default");
        }

        return currencyMessageConfig;
    }
}
