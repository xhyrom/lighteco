package dev.xhyrom.lighteco.common.config.message;

import eu.okaeri.configs.OkaeriConfig;

import java.util.Collections;
import java.util.Map;

public class MessageConfig extends OkaeriConfig {
    public Map<String, CurrencyMessageConfig> currency = Collections.singletonMap("default", new CurrencyMessageConfig());

    public String wait = "<red>Please wait a moment before using this command again.";
    public String userNotFound = "<red>User <username> not found.";
}
