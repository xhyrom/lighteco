package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.common.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ApiPlatform implements Platform {
    private final LightEcoPlugin plugin;

    public ApiPlatform(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NonNull Type getType() {
        return this.plugin.getPlatformType();
    }
}
