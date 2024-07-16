package dev.xhyrom.lighteco.paper;

import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import dev.xhyrom.lighteco.paper.logger.PaperLogger;

import lombok.Getter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class PaperLightEcoBootstrap implements LightEcoBootstrap, LoaderBootstrap {
    private final PaperLightEcoPlugin plugin = new PaperLightEcoPlugin(this);

    @Getter
    private final JavaPlugin loader;

    @Getter
    private final PluginLogger logger;

    @Getter
    private final SchedulerAdapter scheduler;

    @Getter
    private BukkitAudiences audience;

    public PaperLightEcoBootstrap(JavaPlugin loader) {
        this.loader = loader;
        this.logger = new PaperLogger(this.plugin, loader.getLogger());
        this.scheduler = new PaperSchedulerAdapter(this);
    }

    @Override
    public void onLoad() {
        this.plugin.load();
    }

    @Override
    public void onEnable() {
        this.plugin.enable();

        this.audience = BukkitAudiences.create(this.loader);
    }

    @Override
    public void onDisable() {
        this.plugin.disable();
    }

    @Override
    public Path getDataDirectory() {
        return this.loader.getDataFolder().toPath();
    }

    @Override
    public String getVersion() {
        return this.loader.getDescription().getVersion();
    }

    @Override
    public Optional<UUID> lookupUniqueId(String username) {
        return Optional.of(this.loader.getServer().getOfflinePlayer(username))
                .map(OfflinePlayer::getUniqueId);
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
