package dev.xhyrom.lighteco.bukkittest;

import dev.xhyrom.lighteco.api.model.currency.Currency;

import java.math.BigDecimal;

public class TestCurrency implements Currency {
    @Override
    public String getIdentifier() {
        return "test";
    }

    @Override
    public Type getType() {
        return Type.LOCAL;
    }

    @Override
    public boolean isPayable() {
        return true;
    }

    @Override
    public BigDecimal getDefaultBalance() {
        return BigDecimal.ZERO;
    }
}
