package dev.xhyrom.lighteco.common.model.user;

import dev.xhyrom.lighteco.api.exception.CannotBeGreaterThan;
import dev.xhyrom.lighteco.api.exception.CannotBeNegative;
import dev.xhyrom.lighteco.common.api.impl.ApiUser;
import dev.xhyrom.lighteco.common.cache.RedisBackedMap;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class User {
    private final LightEcoPlugin plugin;

    @Getter
    private final ApiUser proxy = new ApiUser(this);

    @Getter
    private final UUID uniqueId;

    @Getter
    @Setter
    private boolean dirty = false;
    @Getter
    @Setter
    private String username;

    private final HashMap<Currency, BigDecimal> balances = new RedisBackedMap<>();

    public User(LightEcoPlugin plugin, UUID uniqueId) {
        this(plugin, uniqueId, null);
    }

    public User(LightEcoPlugin plugin, UUID uniqueId, String username) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.username = username;
    }

    public BigDecimal getBalance(@NonNull Currency currency) {
        return balances.getOrDefault(currency, currency.getDefaultBalance());
    }

    public void setBalance(@NonNull Currency currency, @NonNull BigDecimal balance) throws CannotBeNegative, CannotBeGreaterThan {
        this.setBalance(currency, balance, false);
    }

    public void setBalance(@NonNull Currency currency, @NonNull BigDecimal balance, boolean force) throws CannotBeNegative, CannotBeGreaterThan {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new CannotBeNegative("Balance cannot be negative");
        }

        if (balance.compareTo(this.plugin.getConfig().maximumBalance) > 0) {
            throw new CannotBeGreaterThan("Balance cannot be greater than " + this.plugin.getConfig().maximumBalance);
        }

        balance = balance.setScale(currency.fractionalDigits(), RoundingMode.DOWN);
        balances.put(currency, balance);

        if (!force)
            this.setDirty(true);
    }

    public void deposit(@NonNull Currency currency, @NonNull BigDecimal amount) throws CannotBeNegative, CannotBeGreaterThan {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (amount.compareTo(this.plugin.getConfig().maximumBalance) > 0) {
            throw new CannotBeGreaterThan("Amount cannot be greater than " + this.plugin.getConfig().maximumBalance);
        }

        this.setBalance(currency, this.getBalance(currency).add(amount));
    }

    public void withdraw(@NonNull Currency currency, @NonNull BigDecimal amount) throws CannotBeNegative, CannotBeGreaterThan {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (amount.compareTo(this.plugin.getConfig().maximumBalance) > 0) {
            throw new CannotBeGreaterThan("Amount cannot be greater than " + this.plugin.getConfig().maximumBalance);
        }

        if (this.getBalance(currency).compareTo(amount) < 0) {
            // Withdraw all
            amount = this.getBalance(currency);
        }

        this.setBalance(currency, this.getBalance(currency).subtract(amount));
    }
}
