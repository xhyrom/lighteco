package dev.xhyrom.lighteco.bukkit.logger;

import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitLogger implements PluginLogger {
    private final Logger logger;

    public BukkitLogger(Logger logger) {
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
