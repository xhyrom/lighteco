package dev.xhyrom.lighteco.common.config.storage;

import dev.xhyrom.lighteco.common.storage.StorageType;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class StorageConfig extends OkaeriConfig {
    @Comment("Storage provider.")
    @Comment("Available providers: h2, sqlite, mysql, mariadb")
    public StorageType provider = StorageType.H2;

    @Comment("Data storage settings.")
    @Comment("You don't need to worry about this if you're using local database.")
    public StorageDataConfig data = new StorageDataConfig();

    @Comment("Table prefix.")
    public String tablePrefix = "lighteco_";
}
