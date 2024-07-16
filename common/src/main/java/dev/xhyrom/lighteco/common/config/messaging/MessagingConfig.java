package dev.xhyrom.lighteco.common.config.messaging;

import dev.xhyrom.lighteco.common.messaging.MessagingType;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class MessagingConfig extends OkaeriConfig {
    @Comment("Messaging provider.")
    @Comment("Available providers: redis")
    public MessagingType provider = MessagingType.NONE;

    @Comment("Data storage settings.")
    @Comment("You don't need to worry about this if you're using plugin message.")
    public MessagingDataConfig data = new MessagingDataConfig();
}
