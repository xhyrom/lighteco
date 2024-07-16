package dev.xhyrom.lighteco.api.manager;

import dev.xhyrom.lighteco.api.model.currency.Currency;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface CommandManager {
    // Make more transparent and freedom way to do this (more abstract)

    /**
     * Registers commands for a currency.
     *
     * @param currency the currency to register the command for
     */
    void registerCurrencyCommand(@NonNull Currency currency);

    /**
     * Registers commands for a currency.
     * <p>
     *     If main is true, the pay and balance commands will be exposed as main commands.
     *     You can also use {@link #registerCurrencyCommand(Currency)} to register the commands as subcommands.
     * </p>
     *
     * @param currency the currency to register the command for
     * @param main if pay and balance should be exposed as main commands
     */
    void registerCurrencyCommand(@NonNull Currency currency, boolean main);
}
