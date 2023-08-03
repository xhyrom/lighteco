package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.common.api.LightEcoApi;
import dev.xhyrom.lighteco.common.storage.Storage;
import dev.xhyrom.lighteco.common.storage.StorageFactory;
import lombok.Getter;

public abstract class AbstractLightEcoPlugin implements LightEcoPlugin {
    @Getter
    private Storage storage;
    private LightEcoApi api;

    public final void enable() {
        // setup storage
        StorageFactory factory = new StorageFactory(this);
        this.storage = factory.get();

        // register listeners
        this.registerListeners();

        // setup managers
        this.setupManagers();

        // register api
        this.api = new LightEcoApi(this);
        LightEcoProvider.set(this.api);
        this.registerApiOnPlatform(this.api);
    }

    protected abstract void registerListeners();

    protected abstract void setupManagers();
    protected abstract void registerApiOnPlatform(LightEco api);
}
