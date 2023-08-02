package dev.xhyrom.lighteco.common.managers.currency;

import dev.xhyrom.lighteco.common.managers.AbstractManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

import static dev.xhyrom.lighteco.api.model.currency.Currency.Type;

public class StandardCurrencyManager extends AbstractManager<String, Currency<?>> implements CurrencyManager {
    private final LightEcoPlugin plugin;

    public StandardCurrencyManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Currency<?> apply(String identifier) {
        return null;
    }

    @Override
    public Currency<?> getOrMake(String identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Collection<Currency<?>> getRegisteredCurrencies() {
        return this.values();
    }

    @Override
    public void registerCurrency(@NonNull Currency<?> currency) {
        if (currency.getType() == Type.GLOBAL && this.plugin.getPlatformType().isLocal())
            throw new IllegalArgumentException("Cannot register global currency on local platform");

        if (currency.getType() == Type.LOCAL && this.plugin.getPlatformType().isProxy())
            throw new IllegalArgumentException("Cannot register local currency on proxy platform");

        if (this.isLoaded(currency.getIdentifier()))
            throw new IllegalArgumentException("Currency with identifier " + currency.getIdentifier() + " already registered");

        this.map.put(currency.getIdentifier(), currency);
    }
}
