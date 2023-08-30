package dev.xhyrom.lighteco.bukkit.hooks;

import org.bukkit.Bukkit;

public class Hooks {
    private static PlaceholderAPIExpansion placeholderAPIExpansion;

    public static void register() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            placeholderAPIExpansion.register();
    }

    public static void unregister() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            placeholderAPIExpansion.unregister();
    }
}
