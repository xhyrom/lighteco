package dev.xhyrom.lighteco.bukkit.hooks;

import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Hooks {
    private static PlaceholderAPIExpansion placeholderAPIExpansion;

    public static void register(BukkitLightEcoPlugin plugin) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPIExpansion = new PlaceholderAPIExpansion(plugin);
            placeholderAPIExpansion.register();
        }
    }

    public static void unregister() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            placeholderAPIExpansion.unregister();
    }
}
