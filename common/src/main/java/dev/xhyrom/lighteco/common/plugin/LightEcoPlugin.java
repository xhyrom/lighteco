package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.managers.currency.CurrencyManager;
import dev.xhyrom.lighteco.common.managers.user.UserManager;
import dev.xhyrom.lighteco.common.plugin.bootstrap.LightEcoBootstrap;
import dev.xhyrom.lighteco.common.storage.Storage;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface LightEcoPlugin {
    Platform.@NonNull Type getPlatformType();

    @NonNull LightEcoBootstrap getBootstrap();

    @NonNull UserManager getUserManager();
    @NonNull CurrencyManager getCurrencyManager();
    @NonNull ContextManager<?> getContextManager();

    @NonNull Storage getStorage();
}
