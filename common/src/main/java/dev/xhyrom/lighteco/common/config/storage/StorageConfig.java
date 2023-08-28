package dev.xhyrom.lighteco.common.config.storage;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Variable;

public class StorageConfig extends OkaeriConfig {
    @Comment("Storage provider.")
    @Comment("Available providers: h2, sqlite")
    public String provider = "h2";

    @Comment("Data storage settings.")
    @Comment("You don't need to worry about this if you're using local database.")
    public StorageDataConfig data = new StorageDataConfig();

    @Variable("table-prefix")
    @Comment("Table prefix.")
    public String tablePrefix = "lighteco_";
}
