package dev.xhyrom.lighteco.api.manager;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CommandManager {
    // Make more transparent and freedom way to do this (more abstract)
    void registerCurrencyCommand(@NonNull Currency currency);
    void registerCurrencyCommand(@NonNull Currency currency, boolean main);
}
