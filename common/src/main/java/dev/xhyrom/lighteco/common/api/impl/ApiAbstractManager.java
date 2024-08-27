package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

public abstract class ApiAbstractManager<H> {
    protected final LightEcoPlugin plugin;
    protected final H handle;

    protected ApiAbstractManager(LightEcoPlugin plugin, H handle) {
        this.plugin = plugin;
        this.handle = handle;
    }
}
