package dev.xhyrom.lighteco.bukkit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.xhyrom.lighteco.bukkit.hooks.PlaceholderAPIExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

// Used inside plugin.yml
@SuppressWarnings("unused")
public class BukkitLightEcoLoader extends JavaPlugin {
    private final BukkitLightEcoBootstrap bootstrap;

    public BukkitLightEcoLoader() {
        this.bootstrap = new BukkitLightEcoBootstrap(this);
    }

    @Override
    public void onLoad() {
        this.bootstrap.onLoad();
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .verboseOutput(this.bootstrap.getPlugin().getConfig().debug)
        );
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        this.bootstrap.onEnable();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.bootstrap.onDisable();
    }
}
