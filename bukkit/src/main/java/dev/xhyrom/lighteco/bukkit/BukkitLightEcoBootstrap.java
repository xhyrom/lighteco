package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.bukkit.logger.BukkitLogger;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Getter
public class BukkitLightEcoBootstrap implements LightEcoBootstrap, LoaderBootstrap {
    private final JavaPlugin loader;
    @Getter
    private final BukkitLightEcoPlugin plugin = new BukkitLightEcoPlugin(this);

    @Getter
    private final PluginLogger logger;

    public BukkitLightEcoBootstrap(JavaPlugin loader) {
        this.loader = loader;

        this.logger = new BukkitLogger(loader.getSLF4JLogger());
    }

    @Override
    public void onLoad() {
        plugin.load();
    }

    @Override
    public void onEnable() {
        plugin.enable();
    }

    @Override
    public void onDisable() {

    }

    public Server getServer() {
        return loader.getServer();
    }

    @Override
    public File getDataFolder() {
        return loader.getDataFolder();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        return getServer().getOnlinePlayers().stream()
                .map(Entity::getUniqueId)
                .toList();
    }
}
