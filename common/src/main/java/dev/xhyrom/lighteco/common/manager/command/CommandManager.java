package dev.xhyrom.lighteco.common.manager.command;

import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CommandManager {
    void registerCurrencyCommand(@NonNull Currency currency);

    void onBalance(User sender, Currency currency);
}
