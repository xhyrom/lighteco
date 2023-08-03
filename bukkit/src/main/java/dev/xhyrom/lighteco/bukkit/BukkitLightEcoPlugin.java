package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.bukkit.listeners.BukkitConnectionListener;
import dev.xhyrom.lighteco.bukkit.managers.BukkitContextManager;
import dev.xhyrom.lighteco.common.managers.currency.StandardCurrencyManager;
import dev.xhyrom.lighteco.common.plugin.AbstractLightEcoPlugin;
import dev.xhyrom.lighteco.common.managers.user.StandardUserManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitLightEcoPlugin extends AbstractLightEcoPlugin {
    @Getter
    private final BukkitLightEcoBootstrap bootstrap;

    @Getter
    private StandardUserManager userManager;
    @Getter
    private StandardCurrencyManager currencyManager;
    @Getter
    private ContextManager<Player> contextManager;

    public BukkitLightEcoPlugin(BukkitLightEcoBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void registerListeners() {
        this.bootstrap.getServer().getPluginManager().registerEvents(new BukkitConnectionListener(this), this.bootstrap.getLoader());
    }

    @Override
    public void setupManagers() {
        this.userManager = new StandardUserManager(this);
        this.currencyManager = new StandardCurrencyManager(this);
        this.contextManager = new BukkitContextManager();
    }

    @Override
    public Platform.@NonNull Type getPlatformType() {
        return Platform.Type.BUKKIT;
    }
}
