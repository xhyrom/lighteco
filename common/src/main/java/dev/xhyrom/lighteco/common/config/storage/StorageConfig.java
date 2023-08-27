package dev.xhyrom.lighteco.common.config.storage;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Variable;

public class StorageConfig extends OkaeriConfig {
    @Comment("Storage provider.")
    @Comment("Available providers: memory")
    public String provider = "memory";

    @Comment("Data storage settings.")
    public StorageDataConfig data = new StorageDataConfig();

    @Variable("table-prefix")
    @Comment("Table prefix.")
    public String tablePrefix = "lighteco_";
}
