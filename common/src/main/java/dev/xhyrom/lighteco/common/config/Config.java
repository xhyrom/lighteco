package dev.xhyrom.lighteco.common.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;

@Header("LightEco configuration file.")
public class Config extends OkaeriConfig {
    @Comment("This property must be unique for each server.")
    @Comment("If you have multiple servers, you must set this property to a different value for each server.")
    @Comment("Used for local currencies.")
    public String server = "none";
}
