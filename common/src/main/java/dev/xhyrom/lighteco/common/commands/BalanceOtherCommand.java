package dev.xhyrom.lighteco.common.commands;

import dev.xhyrom.lighteco.common.command.argument.Arguments;
import dev.xhyrom.lighteco.common.command.argument.type.OfflineUserArgument;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;

public class BalanceOtherCommand extends BalanceCommand {
    private final Currency currency;

    public BalanceOtherCommand(@Nullable String permission, @NonNull Currency currency) {
        super(permission + ".balance.others", currency, new OfflineUserArgument("target"));

        this.currency = currency;
    }

    @Override
    public void execute(LightEcoPlugin plugin, CommandSender sender, Arguments args) {
        User target = args.offlineUser("target");
        BigDecimal balance = target.getBalance(currency);

        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        getConfig(plugin, currency).balanceOthers,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("balance", balance.toPlainString())
                )
        );
    }
}
