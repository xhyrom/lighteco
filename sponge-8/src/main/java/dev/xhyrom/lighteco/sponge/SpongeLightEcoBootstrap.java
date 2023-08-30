package dev.xhyrom.lighteco.sponge;

import com.google.inject.Inject;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Plugin("lighteco-sponge")
public class SpongeLightEcoBootstrap implements LightEcoBootstrap, LoaderBootstrap {
    private final SpongeLightEcoPlugin plugin;

    @Inject
    private Logger logger;

    public SpongeLightEcoBootstrap() {
        this.plugin = new SpongeLightEcoPlugin(this);
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
        return null;
    }

    @Override
    public PluginLogger getLogger() {
        return null;
    }

    @Override
    public SchedulerAdapter getScheduler() {
        return null;
    }

    @Override
    public Path getDataDirectory() {
        return null;
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
