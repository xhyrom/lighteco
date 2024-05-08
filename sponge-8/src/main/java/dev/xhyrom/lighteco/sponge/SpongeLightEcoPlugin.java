package dev.xhyrom.lighteco.sponge;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.manager.ContextManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.manager.command.CommandManager;
import dev.xhyrom.lighteco.common.manager.currency.StandardCurrencyManager;
import dev.xhyrom.lighteco.common.manager.user.StandardUserManager;
import dev.xhyrom.lighteco.common.messaging.MessagingFactory;
import dev.xhyrom.lighteco.common.plugin.AbstractLightEcoPlugin;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.entity.living.player.Player;

@Getter
public class SpongeLightEcoPlugin extends AbstractLightEcoPlugin {
    private final SpongeLightEcoBootstrap bootstrap;

    @Getter
    private StandardUserManager userManager;
    @Getter
    private StandardCurrencyManager currencyManager;
    @Getter
    private CommandManager commandManager;
    @Getter
    private ContextManager<Player> contextManager;

    public SpongeLightEcoPlugin(SpongeLightEcoBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    protected void registerListeners() {
        //this.bootstrap.getServer().getPluginManager().registerEvents(new BukkitConnectionListener(this), this.bootstrap);
    }

    @Override
    public void setupManagers() {
        this.userManager = new StandardUserManager(this);
        this.currencyManager = new StandardCurrencyManager(this);
        //this.commandManager = new BukkitCommandManager(this);
        //this.contextManager = new BukkitContextManager();
    }

    @Override
    protected MessagingFactory getMessagingFactory() {
        return new MessagingFactory(this);
    }

    @Override
    protected void registerApiOnPlatform(LightEco api) {
        //this.getBootstrap().getServer().getServicesManager().register(LightEco.class, api, this.getBootstrap(), ServicePriority.Normal);
    }

    @Override
    protected void registerPlatformHooks() {

    }

    @Override
    protected void removePlatformHooks() {

    }

    @Override
    public Platform.@NonNull Type getPlatformType() {
        return Platform.Type.SPONGE;
    }
}
