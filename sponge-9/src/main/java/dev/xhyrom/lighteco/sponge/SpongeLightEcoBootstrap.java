package dev.xhyrom.lighteco.sponge;

import com.google.inject.Inject;

import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;

import dev.xhyrom.lighteco.sponge.logger.SpongeLogger;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;

import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class SpongeLightEcoBootstrap implements LightEcoBootstrap, LoaderBootstrap {
    private final SpongeLightEcoPlugin plugin = new SpongeLightEcoPlugin(this);

    @Getter
    private final SpongeLightEcoLoader loader;

    @Getter
    private final PluginLogger logger;

    @Getter
    private final SchedulerAdapter scheduler;

    private final Game game;
    private final PluginContainer pluginContainer;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;

    public SpongeLightEcoBootstrap(SpongeLightEcoLoader loader) {
        this.loader = loader;
        this.logger = new SpongeLogger(this.plugin, loader.getLogger());
        this.scheduler = new SpongeSchedulerAdapter(this);

        this.game = loader.getInjector().getInstance(Game.class);
        this.pluginContainer = loader.getInjector().getInstance(PluginContainer.class);
    }

    @Override
    public void onLoad() {
        this.plugin.load();
    }

    @Override
    public void onEnable() {
        this.plugin.enable();
    }

    @Override
    public void onDisable() {
        this.plugin.disable();
    }

    @Override
    public Object getLoader() {
        return this.loader;
    }

    @Override
    public PluginLogger getLogger() {
        return this.logger;
    }

    @Override
    public SchedulerAdapter getScheduler() {
        return this.scheduler;
    }

    @Override
    public Path getDataDirectory() {
        return null;
    }

    @Override
    public String getVersion() {
        return this.pluginContainer.metadata().version().toString();
    }

    @Override
    public Optional<UUID> lookupUniqueId(String username) {
        return Optional.empty();
    }

    @Override
    public boolean isPlayerOnline(UUID uniqueId) {
        return false;
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        return game.server().onlinePlayers().size();
    }

    @Override
    public InputStream getResourceStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    @Override
    public Audience getPlayerAudience(UUID uniqueId) {
        return null;
    }
}
