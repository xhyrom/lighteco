package dev.xhyrom.lighteco.sponge;

import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import dev.xhyrom.lighteco.sponge.logger.SpongeLogger;
import lombok.Getter;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class SpongeLightEcoBootstrap implements LightEcoBootstrap, LoaderBootstrap {
    private final SpongeLightEcoPlugin plugin = new SpongeLightEcoPlugin(this);

    @Getter
    private final SpongeLightEcoLoader loader;
    @Getter
    private final PluginLogger logger;
    @Getter
    private final SchedulerAdapter scheduler;

    public SpongeLightEcoBootstrap(SpongeLightEcoLoader loader) {
        this.loader = loader;
        this.logger = new SpongeLogger(loader.logger);
        this.scheduler = new SpongeSchedulerAdapter(this);
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
    public Path getDataDirectory() {
        return null;
    }

    @Override
    public boolean isPlayerOnline(UUID uniqueId) {
        return false;
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        return null;
    }

    @Override
    public InputStream getResourceStream(String filename) {
        return null;
    }
}
