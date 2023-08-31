package dev.xhyrom.lighteco.common.config.housekeeper;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.util.concurrent.TimeUnit;

public class HousekeeperConfig extends OkaeriConfig {
    @Comment("How long should the cache be kept after the last write")
    public int expireAfterWrite = 300;
    public TimeUnit expireAfterWriteUnit = TimeUnit.SECONDS;

    @Comment("How often should housekeeper run")
    public int runInterval = 60;
    public TimeUnit runIntervalUnit = TimeUnit.SECONDS;
}
