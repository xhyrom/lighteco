package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.manager.ContextManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.bukkit.hooks.Hooks;
import dev.xhyrom.lighteco.bukkit.listeners.BukkitCommandSuggestionsListener;
import dev.xhyrom.lighteco.bukkit.listeners.BukkitConnectionListener;
import dev.xhyrom.lighteco.bukkit.manager.BukkitCommandManager;
import dev.xhyrom.lighteco.bukkit.manager.BukkitContextManager;
import dev.xhyrom.lighteco.common.manager.currency.StandardCurrencyManager;
import dev.xhyrom.lighteco.common.messaging.MessagingFactory;
import dev.xhyrom.lighteco.common.plugin.AbstractLightEcoPlugin;
import dev.xhyrom.lighteco.common.manager.user.StandardUserManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
public class BukkitLightEcoPlugin extends AbstractLightEcoPlugin {
    private final BukkitLightEcoBootstrap bootstrap;

    @Getter
    private StandardUserManager userManager;
    @Getter
    private StandardCurrencyManager currencyManager;
    @Getter
    private BukkitCommandManager commandManager;
    @Getter
    private ContextManager<Player> contextManager;

    public BukkitLightEcoPlugin(BukkitLightEcoBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void registerListeners() {
        this.bootstrap.getLoader().getServer().getPluginManager().registerEvents(new BukkitConnectionListener(this), this.bootstrap.getLoader());
        //this.bootstrap.getLoader().getServer().getPluginManager().registerEvents(new BukkitCommandSuggestionsListener(this), this.bootstrap.getLoader());
    }

    @Override
    public void setupManagers() {
        this.userManager = new StandardUserManager(this);
        this.currencyManager = new StandardCurrencyManager(this);
        this.commandManager = new BukkitCommandManager(this);
        this.contextManager = new BukkitContextManager();
    }

    @Override
    protected MessagingFactory getMessagingFactory() {
        return new MessagingFactory(this);
    }

    @Override
    protected void registerApiOnPlatform(LightEco api) {
        this.getBootstrap().getLoader().getServer().getServicesManager().register(LightEco.class, api, this.getBootstrap().getLoader(), ServicePriority.Normal);
    }

    @Override
    protected void registerPlatformHooks() {
        Hooks.register(this);
    }

    @Override
    protected void removePlatformHooks() {
        Hooks.unregister();
    }

    @Override
    public Platform.@NonNull Type getPlatformType() {
        return Platform.Type.BUKKIT;
    }
}
