package dev.xhyrom.lighteco.common.model.user;

import dev.xhyrom.lighteco.common.api.impl.ApiUser;
import dev.xhyrom.lighteco.common.cache.TypedMap;
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
    private final TypedMap<Currency<?>> balances = new TypedMap<>();

    public User(LightEcoPlugin plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
    }

    public <T> T getBalance(@NonNull Currency<?> currency) {
        return balances.<T>getOrDefault(currency, (T) currency.getDefaultBalance());
    }

    public <T> void setBalance(@NonNull Currency<?> currency, @NonNull T balance) {
        balances.put(currency, balance);
    }

    public void invalidateCaches() {
        balances.clear();
    }
}
