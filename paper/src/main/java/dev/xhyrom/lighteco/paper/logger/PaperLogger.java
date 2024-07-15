package dev.xhyrom.lighteco.paper.logger;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PaperLogger implements PluginLogger {
    private final LightEcoPlugin plugin;
    private final Logger logger;

    public PaperLogger(LightEcoPlugin plugin, Logger logger) {
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
        this.logger.log(Level.WARNING, message);
    }

    @Override
    public void warn(String message, Object... args) {
        this.logger.log(Level.WARNING, String.format(message, args));
    }

    @Override
    public void warn(String message, Throwable throwable) {
        this.logger.log(Level.WARNING, message, throwable);
    }

    @Override
    public void warn(String message, Throwable throwable, Object... args) {
        this.logger.log(Level.WARNING, String.format(message, args), throwable);
    }

    @Override
    public void error(String message) {
        this.logger.severe(message);
    }

    @Override
    public void error(String message, Object... args) {
        this.logger.severe(String.format(message, args));
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.logger.log(Level.SEVERE, message, throwable);
    }

    @Override
    public void error(String message, Throwable throwable, Object... args) {
        this.logger.log(Level.SEVERE, String.format(message, args), throwable);
    }
}
