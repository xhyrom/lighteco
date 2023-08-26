package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.manager.ContextManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.config.Config;
import dev.xhyrom.lighteco.common.manager.command.CommandManager;
import dev.xhyrom.lighteco.common.manager.currency.CurrencyManager;
import dev.xhyrom.lighteco.common.manager.user.UserManager;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.storage.Storage;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface LightEcoPlugin {
    Platform.@NonNull Type getPlatformType();

    @NonNull LightEcoBootstrap getBootstrap();

    @NonNull Config getConfig();
    @NonNull UserManager getUserManager();
    @NonNull CurrencyManager getCurrencyManager();
    @NonNull CommandManager getCommandManager();
    @NonNull ContextManager<?> getContextManager();

    @NonNull Storage getStorage();
}
