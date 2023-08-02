package dev.xhyrom.lighteco.bukkit.test;

import dev.xhyrom.lighteco.api.model.currency.Currency;

public class TestCurrency extends Currency<Integer> {
    public TestCurrency() {
        super(Integer.class);
    }

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
}
