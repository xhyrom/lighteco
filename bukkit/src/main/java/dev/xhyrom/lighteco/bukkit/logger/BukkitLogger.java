package dev.xhyrom.lighteco.bukkit.logger;

import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import org.slf4j.Logger;

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
        this.logger.warn(message);
    }

    @Override
    public void warn(String message, Object... args) {
        this.logger.warn(String.format(message, args));
    }

    @Override
    public void warn(String message, Throwable throwable) {
        this.logger.warn(message, throwable);
    }

    @Override
    public void warn(String message, Throwable throwable, Object... args) {
        this.logger.warn(String.format(message, args), throwable);
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
        this.logger.error(message, throwable);
    }

    @Override
    public void error(String message, Throwable throwable, Object... args) {
        this.logger.error(String.format(message, args), throwable);
    }
}
