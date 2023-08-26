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

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("TestPlugin loaded!");

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();

        currencyManager.registerCurrency(new TestCurrency());
        currencyManager.registerCurrency(new TestCurrency2());

        getLogger().info("TestCurrency registered!");

        currencyManager.getRegisteredCurrencies().forEach(currency -> {
            getLogger().info("Currency: " + currency.getIdentifier() + " (" + currency.getType() + ", " + currency.getDecimalPlaces() + ", " + currency.isPayable() + ")");
        });
    }

    @EventHandler
    public void onWalk(AsyncPlayerChatEvent event) throws ParseException {
        Player player = event.getPlayer();

        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();

        User user = provider.getPlayerAdapter(Player.class).getUser(player);

        String message = event.getMessage();
        String command = message.split(" ")[0];
        String[] args = message.substring(command.length()).trim().split(" ");

        Currency currency = currencyManager.getCurrency(args[0]);

        switch (command) {
            case "balance" -> player.sendMessage(user.getBalance(currency).toString());
            case "add" -> {
                if (currency.getDecimalPlaces() > 0) {
                    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                    user.setBalance(currency, BigDecimal.valueOf(
                            Double.parseDouble(
                                    decimalFormat.format(
                                            Double.parseDouble(args[1])
                                    )
                            )
                    ));
                } else {
                    user.setBalance(currency, BigDecimal.valueOf(Integer.parseInt(args[1])));
                }

                provider.getUserManager().saveUser(user).thenAccept(aVoid -> player.sendMessage("Saved!"));
            }
        }
    }
}
