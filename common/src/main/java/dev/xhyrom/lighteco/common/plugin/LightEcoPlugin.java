package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import dev.xhyrom.lighteco.api.managers.UserManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.storage.Storage;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface LightEcoPlugin {
    Platform.@NonNull Type getPlatformType();

    @NonNull UserManager getUserManager();
    @NonNull ContextManager<?> getContextManager();

    @NonNull Storage getStorage();
}
