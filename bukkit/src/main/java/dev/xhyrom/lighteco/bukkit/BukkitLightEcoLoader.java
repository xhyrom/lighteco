package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.bukkit.listeners.BukkitConnectionListener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLightEcoLoader extends JavaPlugin {
    private final BukkitLightEcoPlugin plugin = new BukkitLightEcoPlugin();

    @Override
    public void onLoad() {
        plugin.enable();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BukkitConnectionListener(plugin), this);
    }
}
