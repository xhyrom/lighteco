package dev.xhyrom.lighteco.currency.money.bukkit;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.manager.CommandManager;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.currency.money.bukkit.hooks.vault.VaultFactory;
import dev.xhyrom.lighteco.currency.money.common.MoneyCurrency;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMCLoader extends JavaPlugin {
    @Override
    public void onEnable() {
        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();
        CommandManager commandManager = provider.getCommandManager();

        Currency currency = new MoneyCurrency();

        currencyManager.registerCurrency(currency);
        commandManager.registerCurrencyCommand(currency, true);

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            getSLF4JLogger().info("Vault found, hooking...");
            new VaultFactory(this).hook();
        }
    }
}

