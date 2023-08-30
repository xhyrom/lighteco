package dev.xhyrom.lighteco.sponge;

import com.google.inject.Inject;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LoaderBootstrap;
import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("lighteco-sponge")
public class SpongeLightEcoBootstrap implements LoaderBootstrap {
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
}
