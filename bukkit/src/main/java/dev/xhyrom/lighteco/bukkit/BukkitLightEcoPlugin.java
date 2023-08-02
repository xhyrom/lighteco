package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import dev.xhyrom.lighteco.api.managers.CurrencyManager;
import dev.xhyrom.lighteco.api.managers.UserManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.api.impl.ApiCurrencyManager;
import dev.xhyrom.lighteco.common.managers.currency.StandardCurrencyManager;
import dev.xhyrom.lighteco.common.plugin.AbstractLightEcoPlugin;
import dev.xhyrom.lighteco.common.managers.user.StandardUserManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitLightEcoPlugin extends AbstractLightEcoPlugin {
    @Getter
    private StandardUserManager userManager;
    @Getter
    private StandardCurrencyManager currencyManager;
    @Getter
    private ContextManager<Player> contextManager;

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
