package dev.xhyrom.lighteco.common.model.user;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.common.api.impl.ApiUser;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class User {
    @Getter
    private final LightEcoPlugin plugin;
    @Getter
    private final ApiUser proxy = new ApiUser(this);

    @Getter
    private final UUID uniqueId;

    public User(LightEcoPlugin plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
    }

    // TODO: finish
    public <T> T getBalance(@NonNull Currency<T> currency) {
        this.plugin.getCurrencyManager().getIfLoaded(currency.getIdentifier());
        return null;
    }
}
