package dev.xhyrom.lighteco.currency.money.bukkit.hooks.vault;

import dev.xhyrom.lighteco.currency.money.bukkit.BukkitMCLoader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultFactory {
    private final BukkitMCLoader plugin;
    private Vault vault;

    public VaultFactory(BukkitMCLoader plugin) {
        this.plugin = plugin;
    }

    public void hook() {
        if (this.vault == null)
            vault = new Vault();

        ServicesManager manager = Bukkit.getServicesManager();
        manager.register(Economy.class, vault, this.plugin, ServicePriority.Highest);
    }
}
