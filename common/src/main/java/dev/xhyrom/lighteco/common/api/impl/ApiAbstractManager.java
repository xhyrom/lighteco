package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

public abstract class ApiAbstractManager<H> {
    protected final LightEcoPlugin plugin;
    protected final H handler;

    protected ApiAbstractManager(LightEcoPlugin plugin, H handler) {
        this.plugin = plugin;
        this.handler = handler;
    }
}
