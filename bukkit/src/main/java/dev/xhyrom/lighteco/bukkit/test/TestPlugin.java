package dev.xhyrom.lighteco.bukkit.test;

import dev.xhyrom.lighteco.api.LightEcoProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin {
    private final JavaPlugin plugin;

    public TestPlugin(JavaPlugin plugin) {
        this.plugin = plugin;

        plugin.getLogger().info("TestPlugin loaded!");

        LightEcoProvider.get().getCurrencyManager().registerCurrency(new TestCurrency());

        plugin.getLogger().info("TestCurrency registered!");

        LightEcoProvider.get().getCurrencyManager().getRegisteredCurrencies().forEach(currency -> {
            plugin.getLogger().info("Currency: " + currency.getIdentifier() + " (" + currency.getType() + ", " + currency.getValueType() + ", " + currency.isPayable() + ")");
            currency.getTopUsers(5).forEach(user -> {
                plugin.getLogger().info("User: " + user.getUniqueId() + " (" + user.getUsername() + ")");
            });
        });
    }
}
