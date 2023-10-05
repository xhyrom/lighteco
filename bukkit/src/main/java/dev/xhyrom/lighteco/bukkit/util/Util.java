package dev.xhyrom.lighteco.bukkit.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Util {
    public double bigDecimalToDouble(final BigDecimal value) {
        double amount = value.doubleValue();

        // Don't return bigger balance than user actually has
        if (BigDecimal.valueOf(amount).compareTo(value) > 0) {
            amount = Math.nextAfter(amount, Double.NEGATIVE_INFINITY);
        }

        return amount;
    }
}
