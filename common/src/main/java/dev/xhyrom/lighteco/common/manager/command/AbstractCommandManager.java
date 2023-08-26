package dev.xhyrom.lighteco.common.manager.command;

import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.math.BigDecimal;

public abstract class AbstractCommandManager implements CommandManager {
    protected final LightEcoPlugin plugin;

    public AbstractCommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBalance(User sender, Currency currency) {
        BigDecimal balance = sender.getBalance(currency);

        System.out.println("Your balance: " + balance.toPlainString() + " " + currency.getIdentifier());
        //sender.sendMessage("&aYour balance: &e" + balance.toPlainString() + " &a" + currency.getIdentifier() + "&r");
    }
}
