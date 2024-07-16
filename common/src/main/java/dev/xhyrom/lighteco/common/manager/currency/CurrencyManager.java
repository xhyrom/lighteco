package dev.xhyrom.lighteco.common.manager.currency;

import dev.xhyrom.lighteco.common.manager.Manager;
import dev.xhyrom.lighteco.common.model.currency.Currency;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface CurrencyManager extends Manager<String, Currency> {
    @NonNull Collection<Currency> getRegisteredCurrencies();

    void registerCurrency(@NonNull Currency currency);
}
