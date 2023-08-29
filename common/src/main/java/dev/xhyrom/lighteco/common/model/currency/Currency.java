package dev.xhyrom.lighteco.common.model.currency;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;

import java.math.BigDecimal;

public class Currency {
    private final LightEcoPlugin plugin;

    @Getter
    private final dev.xhyrom.lighteco.api.model.currency.Currency proxy;

    public Currency(LightEcoPlugin plugin, dev.xhyrom.lighteco.api.model.currency.Currency proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    public String getIdentifier() {
        return proxy.getIdentifier();
    }

    public dev.xhyrom.lighteco.api.model.currency.Currency.Type getType() {
        return proxy.getType();
    }

    public BigDecimal getDefaultBalance() {
        return proxy.getDefaultBalance();
    }

    public boolean isPayable() {
        return proxy.isPayable();
    }

    public int fractionalDigits() {
        return proxy.fractionalDigits();
    }
}
