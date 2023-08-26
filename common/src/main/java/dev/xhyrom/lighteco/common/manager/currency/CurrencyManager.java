package dev.xhyrom.lighteco.common.manager.currency;

import dev.xhyrom.lighteco.common.manager.Manager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

public interface CurrencyManager extends Manager<String, Currency> {
    @NonNull Collection<Currency> getRegisteredCurrencies();

    void registerCurrency(@NonNull Currency currency);

    List<User> getTopUsers(@NonNull Currency currency, int length);
}
