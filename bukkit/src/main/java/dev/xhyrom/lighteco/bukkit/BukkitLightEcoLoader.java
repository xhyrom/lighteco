package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLightEcoLoader extends JavaPlugin {
    private final LoaderBootstrap bootstrap;

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
