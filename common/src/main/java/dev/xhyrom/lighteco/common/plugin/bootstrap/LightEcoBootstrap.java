package dev.xhyrom.lighteco.common.plugin.bootstrap;

import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;

import java.util.List;
import java.util.UUID;

public interface LightEcoBootstrap {
    Object getLoader();
    PluginLogger getLogger();
    List<UUID> getOnlinePlayers();
}
