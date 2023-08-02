package dev.xhyrom.lighteco.bukkittest;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.managers.CurrencyManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("TestPlugin loaded!");

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();

        currencyManager.registerCurrency(new TestCurrency());

        getLogger().info("TestCurrency registered!");

        currencyManager.getRegisteredCurrencies().forEach(currency -> {
            getLogger().info("Currency: " + currency.getIdentifier() + " (" + currency.getType() + ", " + currency.getValueType() + ", " + currency.isPayable() + ")");
            /*currencyManager.getTopUsers(currency, 5).forEach(user -> {
                plugin.getLogger().info("User: " + user.getUniqueId() + " (" + user.getUsername() + ")");
            });*/
        });
    }

    @EventHandler
    public void onWalk(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();
        Currency<Integer> currency = currencyManager.getCurrency("test");

        User user = provider.getPlayerAdapter(Player.class).getUser(player);

        switch (event.getMessage()) {
            case "balance": {
                player.sendMessage(user.getBalance(currency).toString());
                break;
            }
            case "add": {
                user.setBalance(currency, user.getBalance(currency) + 1);
                provider.getUserManager().saveUser(user).thenAccept(aVoid -> player.sendMessage("Saved!"));
                break;
            }
        }
    }
}
