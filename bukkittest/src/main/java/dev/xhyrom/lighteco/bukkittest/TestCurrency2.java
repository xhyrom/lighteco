package dev.xhyrom.lighteco.bukkittest;

import dev.xhyrom.lighteco.api.model.currency.Currency;

public class TestCurrency2 extends Currency<Double> {
    public TestCurrency2() {
        super(Double.class);
    }

    @Override
    public String getIdentifier() {
        return "test2";
    }

    @Override
    public Type getType() {
        return Type.LOCAL;
    }

    @Override
    public boolean isPayable() {
        return false;
    }

    @Override
    public Double getDefaultBalance() {
        return 0.0;
    }

    @Override
    public Double calculateTax(Double amount) {
        return 0.0;
    }
}
