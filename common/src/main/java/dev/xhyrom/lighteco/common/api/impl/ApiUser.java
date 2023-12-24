package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public class ApiUser implements User {
    public static dev.xhyrom.lighteco.common.model.user.User cast(User u) {
        if (u instanceof ApiUser) {
            return ((ApiUser) u).handle;
        }

        throw new IllegalArgumentException("Cannot cast " + u.getClass().getName() + " to " + ApiUser.class.getName());
    }

    private final dev.xhyrom.lighteco.common.model.user.User handle;

    public ApiUser(dev.xhyrom.lighteco.common.model.user.User handle) {
        this.handle = handle;
    }

    @Override
    public @NonNull UUID getUniqueId() {
        return this.handle.getUniqueId();
    }

    @Override
    public @Nullable String getUsername() {
        return this.handle.getUsername();
    }

    @Override
    public @NonNull BigDecimal getBalance(@NonNull Currency currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handle.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        return this.handle.getBalance(internal);
    }

    @Override
    public void setBalance(@NonNull Currency currency, @NonNull BigDecimal balance) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handle.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handle.setBalance(internal, balance);
    }

    @Override
    public void deposit(@NonNull Currency currency, @NonNull BigDecimal amount) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handle.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handle.deposit(internal, amount);
    }

    @Override
    public void withdraw(@NonNull Currency currency, @NonNull BigDecimal amount) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.handle.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handle.withdraw(internal, amount);
    }

    @Override
    public void sendMessage(Component message) {
        this.handle.sendMessage(message);
    }
}
