package dev.xhyrom.lighteco.bukkit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.xhyrom.lighteco.bukkit.logger.BukkitLogger;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Getter
public class BukkitLightEcoBootstrap implements LightEcoBootstrap, LoaderBootstrap {
    private final BukkitLightEcoPlugin plugin = new BukkitLightEcoPlugin(this);

    @Getter
    private final JavaPlugin loader;
    @Getter
    private final PluginLogger logger;
    @Getter
    private final SchedulerAdapter scheduler;
    private final BukkitAudiences audience;

    public BukkitLightEcoBootstrap(JavaPlugin loader) {
        this.loader = loader;
        this.logger = new BukkitLogger(loader.getLogger());
        this.scheduler = new BukkitSchedulerAdapter(this);

        this.audience = BukkitAudiences.create(loader);
    }

    @Override
    public void onLoad() {
        this.plugin.load();
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this.loader)
                .verboseOutput(this.getPlugin().getConfig().debug)
        );
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        this.plugin.enable();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.plugin.disable();
    }

    @Override
    public Path getDataDirectory() {
        return this.loader.getDataFolder().toPath();
    }

    @Override
    public boolean isPlayerOnline(UUID uniqueId) {
        Player player = this.loader.getServer().getPlayer(uniqueId);
        return player != null && player.isOnline();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        return this.loader.getServer().getOnlinePlayers().stream()
                .map(Entity::getUniqueId)
                .toList();
    }

    @Override
    public InputStream getResourceStream(String filename) {
        return this.loader.getResource(filename);
    }

    @Override
    public Audience getPlayerAudience(UUID uniqueId) {
        return audience.player(uniqueId);
    }
}
