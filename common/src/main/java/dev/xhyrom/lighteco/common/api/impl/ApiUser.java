package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
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
    public @Nullable String getUsername() {
        return this.handler.getUsername();
    }

    @Override
    public @NonNull BigDecimal getBalance(@NonNull Currency currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handler.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        return this.handler.getBalance(internal);
    }

    @Override
    public void setBalance(@NonNull Currency currency, @NonNull BigDecimal balance) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handler.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handler.setBalance(internal, balance);
    }

    @Override
    public void deposit(@NonNull Currency currency, @NonNull BigDecimal amount) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handler.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handler.deposit(internal, amount);
    }

    @Override
    public void withdraw(@NonNull Currency currency, @NonNull BigDecimal amount) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handler.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handler.withdraw(internal, amount);
    }
}
