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

    @Comment("Maximum number of connections in the pool.")
    public int maximumPoolSize = 10;

    @Comment("Minimum idle connections in the pool.")
    public int minimumIdle = 5;

    @Comment("Maximum lifetime of a connection in the pool.")
    public long maxLifetime = 1800000;

    @Comment("Keep alive interval in milliseconds.")
    public long keepAliveTime = 0;

    @Comment("Connection timeout in milliseconds.")
    public long connectionTimeout = 5000;
}
