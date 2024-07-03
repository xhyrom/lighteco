package dev.xhyrom.lighteco.paper;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.manager.ContextManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.paper.hooks.Hooks;
import dev.xhyrom.lighteco.paper.listeners.PaperCommandSuggestionsListener;
import dev.xhyrom.lighteco.paper.listeners.PaperConnectionListener;
import dev.xhyrom.lighteco.paper.manager.PaperCommandManager;
import dev.xhyrom.lighteco.paper.manager.PaperContextManager;
import dev.xhyrom.lighteco.common.manager.currency.StandardCurrencyManager;
import dev.xhyrom.lighteco.common.messaging.MessagingFactory;
import dev.xhyrom.lighteco.common.plugin.AbstractLightEcoPlugin;
import dev.xhyrom.lighteco.common.manager.user.StandardUserManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
public class PaperLightEcoPlugin extends AbstractLightEcoPlugin {
    private final PaperLightEcoBootstrap bootstrap;

    @Getter
    private StandardUserManager userManager;
    @Getter
    private StandardCurrencyManager currencyManager;
    @Getter
    private PaperCommandManager commandManager;
    @Getter
    private ContextManager<Player> contextManager;

    public PaperLightEcoPlugin(PaperLightEcoBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void registerListeners() {
        this.bootstrap.getLoader().getServer().getPluginManager().registerEvents(new PaperConnectionListener(this), this.bootstrap.getLoader());
        this.bootstrap.getLoader().getServer().getPluginManager().registerEvents(new PaperCommandSuggestionsListener(this), this.bootstrap.getLoader());
    }

    @Override
    public void setupManagers() {
        this.userManager = new StandardUserManager(this);
        this.currencyManager = new StandardCurrencyManager(this);
        this.commandManager = new PaperCommandManager(this);
        this.contextManager = new PaperContextManager();
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
