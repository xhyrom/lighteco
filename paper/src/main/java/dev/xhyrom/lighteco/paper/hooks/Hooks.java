package dev.xhyrom.lighteco.paper.hooks;

import dev.xhyrom.lighteco.paper.PaperLightEcoPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Hooks {
    private static PlaceholderAPIExpansion placeholderAPIExpansion;

    public static void register(PaperLightEcoPlugin plugin) {
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
