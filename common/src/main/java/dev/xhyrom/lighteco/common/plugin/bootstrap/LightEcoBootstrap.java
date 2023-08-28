package dev.xhyrom.lighteco.common.plugin.bootstrap;

import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface LightEcoBootstrap {
    Object getLoader();
    PluginLogger getLogger();
    Path getDataDirectory();
    List<UUID> getOnlinePlayers();
    InputStream getResourceStream(String filename);
}
