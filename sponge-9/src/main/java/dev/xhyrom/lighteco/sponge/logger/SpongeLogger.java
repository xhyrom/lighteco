package dev.xhyrom.lighteco.sponge.logger;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class SpongeLogger implements PluginLogger {
    private final LightEcoPlugin plugin;
    private final Logger logger;

    public SpongeLogger(LightEcoPlugin plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void info(String message, Object... args) {
        this.logger.info(String.format(message, args));
    }

    @Override
    public void debug(String message) {
        if (!this.plugin.getConfig().debug) return;

        info(message);
    }

    @Override
    public void debug(String message, Object... args) {
        if (!this.plugin.getConfig().debug) return;

        info(message, args);
    }

    @Override
    public void warn(String message) {
        this.logger.log(Level.WARN, message);
    }

    @Override
    public void warn(String message, Object... args) {
        this.logger.log(Level.WARN, String.format(message, args));
    }

    @Override
    public void warn(String message, Throwable throwable) {
        this.logger.log(Level.WARN, message, throwable);
    }

    @Override
    public void warn(String message, Throwable throwable, Object... args) {
        this.logger.log(Level.WARN, String.format(message, args), throwable);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);
    }

    @Override
    public void error(String message, Object... args) {
        this.logger.error(String.format(message, args));
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.logger.log(Level.ERROR, message, throwable);
    }

    @Override
    public void error(String message, Throwable throwable, Object... args) {
        this.logger.log(Level.ERROR, String.format(message, args), throwable);
    }
}