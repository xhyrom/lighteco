package dev.xhyrom.lighteco.common.managers.currency;

import dev.xhyrom.lighteco.common.managers.Manager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface CurrencyManager extends Manager<String, Currency<?>> {
    @NonNull Collection<Currency<?>> getRegisteredCurrencies();

    void registerCurrency(@NonNull Currency<?> currency);
}
