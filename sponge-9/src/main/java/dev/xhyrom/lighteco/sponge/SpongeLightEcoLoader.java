package dev.xhyrom.lighteco.sponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("lighteco-sponge")
public class SpongeLightEcoLoader {
    private final SpongeLightEcoBootstrap bootstrap;
    @Getter
    private final Injector injector;

    @Getter
    @Inject
    public Logger logger;

    @Inject
    public SpongeLightEcoLoader(Injector injector) {
        this.injector = injector;
        this.bootstrap = new SpongeLightEcoBootstrap(this);
    }

    @Listener(order = Order.FIRST)
    public void onEnable(ConstructPluginEvent event) {
        this.bootstrap.onLoad();
        this.bootstrap.onEnable();
    }

    @Listener
    public void onDisable(StoppingEngineEvent<Server> event) {
        this.bootstrap.onDisable();
    }
}
