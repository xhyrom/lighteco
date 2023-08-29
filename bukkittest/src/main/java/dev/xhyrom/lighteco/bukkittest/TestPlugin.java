package dev.xhyrom.lighteco.bukkittest;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.model.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("TestPlugin loaded!");

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();

        currencyManager.registerCurrency(new TestCurrency());
        currencyManager.registerCurrency(new TestCurrency2());
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("TestCurrency registered!");

        currencyManager.getRegisteredCurrencies().forEach(currency -> getLogger().info("Currency: " + currency.getIdentifier() + " (" + currency.getType() + ", " + currency.fractionalDigits() + ", " + currency.isPayable() + ")"));

        provider.getCommandManager().registerCurrencyCommand(currencyManager.getCurrency("test"));
        provider.getCommandManager().registerCurrencyCommand(currencyManager.getCurrency("test2"));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();
        CompletableFuture<List<User>> topusers = currencyManager.getTopUsers(currencyManager.getCurrency("money"), 5);

        for (User user : topusers.join()) {
            event.getPlayer().sendMessage(user.getUniqueId() + " ("+ user.getUsername() +") " + ": " + user.getBalance(currencyManager.getCurrency("money")));
        }
    }
}
