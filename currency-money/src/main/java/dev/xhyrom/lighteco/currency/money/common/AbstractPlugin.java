package dev.xhyrom.lighteco.currency.money.common;

import dev.xhyrom.lighteco.currency.money.common.config.Config;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class AbstractPlugin implements Plugin {
    private Config config;
    private final Path dataDirectory;

    public AbstractPlugin(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public final void load() {
        this.config = ConfigManager.create(Config.class, it -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(dataDirectory.resolve("config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }
}
