package dev.xhyrom.lighteco.common.plugin;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.common.api.LightEcoApi;
import dev.xhyrom.lighteco.common.config.Config;
import dev.xhyrom.lighteco.common.dependencies.DependencyManager;
import dev.xhyrom.lighteco.common.dependencies.DependencyManagerImpl;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.storage.Storage;
import dev.xhyrom.lighteco.common.storage.StorageFactory;
import dev.xhyrom.lighteco.common.task.UserSaveTask;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public abstract class AbstractLightEcoPlugin implements LightEcoPlugin {
    private DependencyManager dependencyManager;
    @Getter
    private Config config;

    @Getter
    private Storage storage;
    private LightEcoApi api;

    private UserSaveTask userSaveTask;

    public final void load() {
        this.dependencyManager = new DependencyManagerImpl(this);

        this.config = ConfigManager.create(Config.class, it -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(this.getBootstrap().getDataDirectory().resolve("config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }

    public final void enable() {
        // setup storage
        StorageFactory factory = new StorageFactory(this);
        this.dependencyManager.loadStorageDependencies(factory.getRequiredTypes());

        this.storage = factory.get();

        // register listeners
        this.registerListeners();

        // setup managers
        this.setupManagers();

        // register api
        this.api = new LightEcoApi(this);
        LightEcoProvider.set(this.api);
        this.registerApiOnPlatform(this.api);

        this.userSaveTask = new UserSaveTask(this);
        this.getBootstrap().getScheduler().asyncRepeating(userSaveTask, this.config.saveInterval, TimeUnit.SECONDS);
    }

    public final void disable() {
        this.userSaveTask.run(); // save all users synchronously

        this.storage.shutdown();
    }

    protected abstract void registerListeners();

    protected abstract void setupManagers();
    protected abstract void registerApiOnPlatform(LightEco api);
}
