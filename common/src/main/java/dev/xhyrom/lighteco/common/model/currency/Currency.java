package dev.xhyrom.lighteco.common.model.currency;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Currency {
    private final dev.xhyrom.lighteco.api.model.currency.Currency proxy;

    public Currency(dev.xhyrom.lighteco.api.model.currency.Currency proxy) {
        this.proxy = proxy;
    }

    public String getIdentifier() {
        return proxy.getIdentifier();
    }

    public String[] getIdentifierAliases() {
        return proxy.getIdentifierAliases();
    }

    public dev.xhyrom.lighteco.api.model.currency.Currency.Type getType() {
        return proxy.getType();
    }

    public String format(BigDecimal amount) {
        return proxy.format(amount);
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
