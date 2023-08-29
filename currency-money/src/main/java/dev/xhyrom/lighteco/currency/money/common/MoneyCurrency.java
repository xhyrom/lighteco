package dev.xhyrom.lighteco.currency.money.common;

import dev.xhyrom.lighteco.api.model.currency.Currency;

import java.math.BigDecimal;

public class MoneyCurrency implements Currency {
    @Override
    public String getIdentifier() {
        return "money";
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

    @Override
    public int fractionalDigits() {
       return 2;
    }
}
