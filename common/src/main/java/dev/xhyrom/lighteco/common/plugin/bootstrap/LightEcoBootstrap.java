package dev.xhyrom.lighteco.common.plugin.bootstrap;

import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface LightEcoBootstrap {
    Object getLoader();
    PluginLogger getLogger();
    File getDataFolder();
    List<UUID> getOnlinePlayers();
}
