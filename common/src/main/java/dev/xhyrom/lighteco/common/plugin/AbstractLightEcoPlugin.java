package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.common.api.LightEcoApi;
import dev.xhyrom.lighteco.common.storage.Storage;
import dev.xhyrom.lighteco.common.storage.StorageFactory;
import lombok.Getter;

public abstract class AbstractLightEcoPlugin implements LightEcoPlugin {
    @Getter
    private Storage storage;
    private LightEcoApi api;

    public final void load() {
        // setup storage
        StorageFactory factory = new StorageFactory(this);
        this.storage = factory.get();

        // setup managers
        this.setupManagers();

        // register api
        this.api = new LightEcoApi(this);
        LightEcoProvider.set(this.api);
    }

    public final void enable() {
        // register listeners
        this.registerListeners();
    }

    protected abstract void registerListeners();
    protected abstract void setupManagers();
}
