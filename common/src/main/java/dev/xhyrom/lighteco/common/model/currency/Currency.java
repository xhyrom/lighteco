package dev.xhyrom.lighteco.common.model.currency;

import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Currency<T> {
    private final LightEcoPlugin plugin;

    @Getter
    private final dev.xhyrom.lighteco.api.model.currency.Currency<T> proxy;

    public Currency(LightEcoPlugin plugin, dev.xhyrom.lighteco.api.model.currency.Currency<T> proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    public String getIdentifier() {
        return proxy.getIdentifier();
    }

    public dev.xhyrom.lighteco.api.model.currency.Currency.Type getType() {
        return proxy.getType();
    }

    public Class<T> getValueType() {
        return proxy.getValueType();
    }

    public T getDefaultBalance() {
        return proxy.getDefaultBalance();
    }

    public boolean isPayable() {
        return proxy.isPayable();
    }

    public List<User> getTopUsers(int length) {
        List<User> users = new ArrayList<>();
        users.add(new User(plugin, new UUID(0, 0)));

        return users;
    }
}
