package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.bukkit.test.TestPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLightEcoLoader extends JavaPlugin {
    private final BukkitLightEcoPlugin plugin = new BukkitLightEcoPlugin();

    @Override
    public void onEnable() {
        plugin.enable();

        new TestPlugin(this);
    }
}
