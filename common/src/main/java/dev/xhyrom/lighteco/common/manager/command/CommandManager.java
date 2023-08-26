package dev.xhyrom.lighteco.common.manager.command;

import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;

public interface CommandManager {
    void registerCurrencyCommand(@NonNull Currency currency);

    boolean canUse(CommandSender sender);

    void onBalance(CommandSender sender, Currency currency);
    void onBalance(CommandSender sender, Currency currency, User target);

    void onSet(CommandSender sender, Currency currency, User target, BigDecimal amount);
    void onGive(CommandSender sender, Currency currency, User target, BigDecimal amount);
    void onTake(CommandSender sender, Currency currency, User target, BigDecimal amount);
    void onPay(CommandSender sender, Currency currency, User target, BigDecimal amount);
}
