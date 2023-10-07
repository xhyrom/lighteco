package dev.xhyrom.lighteco.bukkit;

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
    }

    @Override
    public void onEnable() {
        this.bootstrap.onEnable();
    }

    @Override
    public void onDisable() {
        this.bootstrap.onDisable();
    }
}
