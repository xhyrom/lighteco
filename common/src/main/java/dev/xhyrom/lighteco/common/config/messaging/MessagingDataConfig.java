package dev.xhyrom.lighteco.common.config.messaging;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class MessagingDataConfig extends OkaeriConfig {
    @Comment("Define the address and port of the messaging service.")
    public String address = "localhost";

    @Comment("Credentials for connecting to the messaging service.")
    public String username = "root";

    public String password = "password";

    @Comment("Whether to use SSL to connect to the messaging service.")
    public boolean ssl = false;
}
