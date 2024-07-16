package dev.xhyrom.lighteco.test.paper;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("TestPlugin loaded!");

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();

        currencyManager.registerCurrency(new TestCurrency());
        currencyManager.registerCurrency(new TestCurrency2());

        getLogger().info("TestCurrency registered!");

        currencyManager.getRegisteredCurrencies().forEach(currency -> getLogger().info("Currency: " + currency.getIdentifier() + " (" + currency.getType() + ", " + currency.fractionalDigits() + ", " + currency.isPayable() + ")"));

        provider.getCommandManager().registerCurrencyCommand(currencyManager.getCurrency("test"));
        provider.getCommandManager().registerCurrencyCommand(currencyManager.getCurrency("test2"));
    }
}
