package dev.xhyrom.lighteco.api.manager;

import dev.xhyrom.lighteco.api.model.currency.Currency;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public interface CurrencyManager {
    /**
     * Gets all registered currencies.
     *
     * @return a collection of all registered currencies
     */
    @NonNull Collection<Currency> getRegisteredCurrencies();

    /**
     * Gets a currency by its identifier.
     *
     * @param identifier the identifier of the currency
     * @return the currency, or null if not found
     */
    @Nullable Currency getCurrency(@NonNull String identifier);

    /**
     * Registers a currency.
     *
     * @param currency the currency to register
     */
    void registerCurrency(@NonNull Currency currency);
}
