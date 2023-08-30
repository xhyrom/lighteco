package dev.xhyrom.lighteco.currency.money.common;

import dev.xhyrom.lighteco.currency.money.common.config.Config;

public interface Plugin {
    void load();

    Config getConfig();
}
