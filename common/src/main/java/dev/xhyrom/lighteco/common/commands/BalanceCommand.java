package dev.xhyrom.lighteco.common.commands;

import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.command.argument.Argument;
import dev.xhyrom.lighteco.common.command.argument.Arguments;
import dev.xhyrom.lighteco.common.command.argument.type.OfflineUserArgument;
import dev.xhyrom.lighteco.common.config.message.CurrencyMessageConfig;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceCommand extends Command {
    private final Currency currency;

    public static BalanceCommand create(@NonNull Currency currency) {
        return new BalanceCommand(currency.getIdentifier() + ".balance", currency);
    }

    public BalanceCommand(@Nullable String permission, @NonNull Currency currency) {
        super("balance", permission, new Argument<?>[0]);

        this.currency = currency;
    }

    @Override
    public void execute(LightEcoPlugin plugin, CommandSender sender, Arguments args) {
        User user = plugin.getUserManager().getIfLoaded(sender.getUniqueId());
        BigDecimal balance = user.getBalance(currency);

        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        getCurrencyMessageConfig(plugin, currency).balance,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("balance", balance.toPlainString())
                )
        );
    }
}
