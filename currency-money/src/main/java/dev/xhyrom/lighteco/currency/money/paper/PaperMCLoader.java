package dev.xhyrom.lighteco.currency.money.paper;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.manager.CommandManager;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.currency.money.paper.hooks.vault.VaultFactory;
import dev.xhyrom.lighteco.currency.money.common.AbstractPlugin;
import dev.xhyrom.lighteco.currency.money.common.Plugin;
import dev.xhyrom.lighteco.currency.money.common.currency.MoneyCurrency;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperMCLoader extends JavaPlugin {
    private VaultFactory vaultFactory;
    @Getter
    private final Plugin plugin;

    public PaperMCLoader() {
        this.plugin = new AbstractPlugin(this.getDataFolder().toPath());
    }

    @Override
    public void onLoad() {
        this.plugin.load();
    }

    @Override
    public void onEnable() {
        LightEco provider = LightEcoProvider.get();
        CurrencyManager currencyManager = provider.getCurrencyManager();
        CommandManager commandManager = provider.getCommandManager();

        Currency currency = new MoneyCurrency(this.plugin);

        currencyManager.registerCurrency(currency);
        commandManager.registerCurrencyCommand(currency, true);

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            getSLF4JLogger().info("Vault found, hooking...");

            this.vaultFactory = new VaultFactory(this);
            this.vaultFactory.hook();
        }
    }

    @Override
    public void onDisable() {
        this.vaultFactory.unhook();
    }
}

