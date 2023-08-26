package dev.xhyrom.lighteco.common.manager.currency;

import dev.xhyrom.lighteco.common.manager.AbstractManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import static dev.xhyrom.lighteco.api.model.currency.Currency.Type;

public class StandardCurrencyManager extends AbstractManager<String, Currency> implements CurrencyManager {
    private final LightEcoPlugin plugin;

    public StandardCurrencyManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Currency apply(String identifier) {
        return null;
    }

    @Override
    public Currency getOrMake(String identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Collection<Currency> getRegisteredCurrencies() {
        return this.values();
    }

    @Override
    public void registerCurrency(@NonNull Currency currency) {
        if (this.isLoaded(currency.getIdentifier()))
            throw new IllegalArgumentException("Currency with identifier " + currency.getIdentifier() + " already registered");

        this.map.put(currency.getIdentifier(), currency);
    }

    // TODO: finish
    @Override
    public List<User> getTopUsers(@NonNull Currency currency, int length) {
        return null;
    }
}
