package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.common.api.LightEcoApi;
import dev.xhyrom.lighteco.common.config.Config;
import dev.xhyrom.lighteco.common.storage.Storage;
import dev.xhyrom.lighteco.common.storage.StorageFactory;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;

import java.io.File;

public abstract class AbstractLightEcoPlugin implements LightEcoPlugin {
    @Getter
    private Storage storage;
    private LightEcoApi api;
    @Getter
    private Config config;

    public final void load() {
        Config config = ConfigManager.create(Config.class, (it) -> {
            File path = new File(this.getBootstrap().getDataFolder(), "config.yml");
            path.mkdir();

            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(path);
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        this.config = config;
    }

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
