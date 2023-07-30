package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.common.storage.Storage;
import dev.xhyrom.lighteco.common.storage.StorageFactory;
import lombok.Getter;

public abstract class AbstractLightEcoPlugin implements LightEcoPlugin {
    @Getter
    private Storage storage;

    public final void enable() {
        StorageFactory factory = new StorageFactory(this);

        this.storage = factory.get();
    }

    protected abstract void setupManagers();
}
