package dev.xhyrom.lighteco.currency.money.common.currency;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.currency.money.common.Plugin;

import java.math.BigDecimal;

public class MoneyCurrency implements Currency {
    public final Plugin plugin;

    public MoneyCurrency(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "money";
    }

    @Override
    public String[] getIdentifierAliases() {
        return new String[] {"eco"};
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
        return this.plugin.getConfig().fractionalDigits;
    }
}
