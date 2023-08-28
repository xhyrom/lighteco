package dev.xhyrom.lighteco.common.storage.provider.sql.connection;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.sql.Connection;

public interface ConnectionFactory {
    void init(LightEcoPlugin plugin);
    void shutdown() throws Exception;

    Connection getConnection() throws Exception;
}
