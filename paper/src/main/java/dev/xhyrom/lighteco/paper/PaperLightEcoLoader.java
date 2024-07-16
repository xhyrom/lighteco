package dev.xhyrom.lighteco.paper;

import org.bukkit.plugin.java.JavaPlugin;

// Used inside plugin.yml
@SuppressWarnings("unused")
public class PaperLightEcoLoader extends JavaPlugin {
    private final PaperLightEcoBootstrap bootstrap;

    public PaperLightEcoLoader() {
        this.bootstrap = new PaperLightEcoBootstrap(this);
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
