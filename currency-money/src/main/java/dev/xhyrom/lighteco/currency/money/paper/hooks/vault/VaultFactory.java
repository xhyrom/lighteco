package dev.xhyrom.lighteco.currency.money.paper.hooks.vault;

import dev.xhyrom.lighteco.currency.money.paper.PaperMCLoader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultFactory {
    private final PaperMCLoader loader;
    private Vault vault;

    public VaultFactory(PaperMCLoader loader) {
        this.loader = loader;
    }

    public void hook() {
        if (this.vault == null)
            vault = new Vault(this.loader.getPlugin());

        ServicesManager manager = Bukkit.getServicesManager();
        manager.register(Economy.class, vault, this.loader, ServicePriority.Highest);
    }

    public void unhook() {
        if (this.vault == null) return;

        ServicesManager manager = Bukkit.getServicesManager();
        manager.unregister(Economy.class, vault);
    }
}
