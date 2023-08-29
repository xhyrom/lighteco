package dev.xhyrom.lighteco.common.config;

import dev.xhyrom.lighteco.common.config.message.MessageConfig;
import dev.xhyrom.lighteco.common.config.storage.StorageConfig;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;

@Header("LightEco configuration file.")
@Header("")
public class Config extends OkaeriConfig {
    @Comment("This property must be unique for each server.")
    @Comment("If you have multiple servers, you must set this property to a different value for each server.")
    @Comment("Used for local currencies.")
    public String server = "none";

    @Comment("Storage settings.")
    public StorageConfig storage = new StorageConfig();

    @Comment("Save interval to storage in seconds.")
    public long saveInterval = 5L;

    @Comment("Messages")
    public MessageConfig messages = new MessageConfig();
}
