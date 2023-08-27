package dev.xhyrom.lighteco.bukkit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.xhyrom.lighteco.bukkit.hooks.PlaceholderAPIExpansion;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLightEcoLoader extends JavaPlugin {
    private final BukkitLightEcoBootstrap bootstrap;

    public BukkitLightEcoLoader() {
        this.bootstrap = new BukkitLightEcoBootstrap(this);
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
        this.bootstrap.onLoad();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        this.bootstrap.onEnable();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PlaceholderAPIExpansion(this.bootstrap.getPlugin()).register();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.bootstrap.onDisable();
    }
}
