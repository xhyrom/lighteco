package dev.xhyrom.lighteco.api.managers;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

public interface CurrencyManager {
    @NonNull Collection<Currency<?>> getRegisteredCurrencies();

    <T> Currency<T> getCurrency(@NonNull String identifier);

    void registerCurrency(@NonNull Currency<?> currency);

    List<User> getTopUsers(@NonNull Currency<?> currency, int length);
}
