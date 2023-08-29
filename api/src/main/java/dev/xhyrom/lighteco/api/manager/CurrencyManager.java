package dev.xhyrom.lighteco.api.manager;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    /**
     * Gets the top users for a currency.
     *
     * @implNote This method is not cached. It fetches the data from the database every time it is called.
     * @param currency the currency to get the top users for
     * @param length the length of the list
     * @return a future that completes with the top users (sorted from highest to lowest balance)
     */
    CompletableFuture<List<User>> getTopUsers(@NonNull Currency currency, int length);
}
