package dev.xhyrom.lighteco.api.managers;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface CurrencyManager {
    @NonNull Collection<Currency<?>> getRegisteredCurrencies();

    Currency<?> getCurrency(@NonNull String identifier);
    void registerCurrency(@NonNull Currency<?> currency);
}
