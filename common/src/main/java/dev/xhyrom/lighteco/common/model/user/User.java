package dev.xhyrom.lighteco.common.model.user;

import dev.xhyrom.lighteco.common.api.impl.ApiUser;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    @Getter
    private final LightEcoPlugin plugin;
    @Getter
    private final ApiUser proxy = new ApiUser(this);

    @Getter
    private final UUID uniqueId;
    private final Map<Currency<?>, Number> balances = new HashMap<>();

    public User(LightEcoPlugin plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
    }

    public <T> T getBalance(@NonNull Currency<T> currency) {
        T balance = (T) balances.get(currency);
        return balance == null ? currency.getDefaultBalance() : balance;
    }

    public <T> void setBalance(@NonNull Currency<T> currency, @NonNull T balance) {
        balances.put(currency, (Number) balance);
    }
}
