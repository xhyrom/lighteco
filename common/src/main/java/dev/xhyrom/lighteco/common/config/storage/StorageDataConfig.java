package dev.xhyrom.lighteco.common.config.storage;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class StorageDataConfig extends OkaeriConfig {
    @Comment("Define the address and port of the database.")
    public String address = "localhost";

    @Comment("The name of the database to use.")
    public String database = "lighteco";

    @Comment("Credentials for connecting to the database.")
    public String username = "root";
    public String password = "password";
}
