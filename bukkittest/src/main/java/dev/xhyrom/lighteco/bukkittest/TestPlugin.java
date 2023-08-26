package dev.xhyrom.lighteco.bukkittest;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("TestPlugin loaded!");

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();

        currencyManager.registerCurrency(new TestCurrency());
        currencyManager.registerCurrency(new TestCurrency2());

        getLogger().info("TestCurrency registered!");

        currencyManager.getRegisteredCurrencies().forEach(currency -> {
            getLogger().info("Currency: " + currency.getIdentifier() + " (" + currency.getType() + ", " + currency.getDecimalPlaces() + ", " + currency.isPayable() + ")");
        });

        provider.getCommandManager().registerCurrencyCommand(currencyManager.getCurrency("test"));
    }
}
