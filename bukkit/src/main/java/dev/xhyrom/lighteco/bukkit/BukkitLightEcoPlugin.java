package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import dev.xhyrom.lighteco.api.managers.UserManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.LightEcoPlugin;
import dev.xhyrom.lighteco.common.managers.StandardUserManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitLightEcoPlugin extends JavaPlugin implements LightEcoPlugin {
    @Getter
    private UserManager userManager;
    @Getter
    private ContextManager<Player> contextManager;

    @Override
    public void onLoad() {
        this.userManager = new StandardUserManager(this);
        this.contextManager = new BukkitContextManager();
    }

    @Override
    public void onEnable() {
        getLogger().info("BukkitLightEco is enabled!");
    }

    @Override
    public Platform.@NonNull Type getPlatformType() {
        return Platform.Type.BUKKIT;
    }
}
