package dev.xhyrom.lighteco.common.manager.currency;

import dev.xhyrom.lighteco.common.manager.SingleManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public class StandardCurrencyManager extends SingleManager<String, Currency>
        implements CurrencyManager {
    private final LightEcoPlugin plugin;

    public StandardCurrencyManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Currency apply(String identifier) {
        throw new UnsupportedOperationException();
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
            throw new IllegalArgumentException(
                    "Currency with identifier " + currency.getIdentifier() + " already registered");

        if (this.plugin.getConfig().debug)
            this.plugin
                    .getBootstrap()
                    .getLogger()
                    .info("Registering currency " + currency.getIdentifier());

        this.plugin.getStorage().registerCurrencySync(currency.getProxy());
        this.map.put(currency.getIdentifier(), currency);
    }
}
