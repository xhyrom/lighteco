package dev.xhyrom.lighteco.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLightEcoLoader extends JavaPlugin {
    private BukkitLightEcoPlugin plugin = new BukkitLightEcoPlugin();

    @Override
    public void onEnable() {
        plugin.enable();
    }
}
