package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class ApiUser implements User {
    private final dev.xhyrom.lighteco.common.model.user.User handler;

    public ApiUser(dev.xhyrom.lighteco.common.model.user.User handler) {
        this.handler = handler;
    }

    @Override
    public @NonNull UUID getUniqueId() {
        return this.handler.getUniqueId();
    }

    @Override
    public @NonNull String getUsername() {
        return null;
    }

    @Override
    public <T> T getBalance(@NonNull Currency<T> currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency<T> internal = (dev.xhyrom.lighteco.common.model.currency.Currency<T>) this.handler.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        return this.handler.getBalance(internal);
    }

    @Override
    public <T> void setBalance(@NonNull Currency<T> currency, @NonNull T balance) {
        dev.xhyrom.lighteco.common.model.currency.Currency<T> internal = (dev.xhyrom.lighteco.common.model.currency.Currency<T>) this.handler.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handler.setBalance(internal, balance);
    }
}
