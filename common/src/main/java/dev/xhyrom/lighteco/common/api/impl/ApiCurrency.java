package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;

import java.util.List;

public class ApiCurrency extends Currency {
    private final dev.xhyrom.lighteco.common.model.currency.Currency<?> handler;

    public ApiCurrency(dev.xhyrom.lighteco.common.model.currency.Currency<?> handler) {
        super(handler.getValueType());

        this.handler = handler;
    }

    public String getIdentifier() {
        return this.handler.getIdentifier();
    }

    public Currency.Type getType() {
        return this.handler.getType();
    }

    public boolean isPayable() {
        return this.handler.isPayable();
    }

    public List<User> getTopUsers(int length) {
        return this.handler.getTopUsers(length)
                .stream()
                .map(ApiUserManager::wrap)
                .toList();
    }
}
