package dev.xhyrom.lighteco.common.storage.provider.sql.connection;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.sql.Connection;
import java.util.function.Function;

public interface ConnectionFactory {
    void init(LightEcoPlugin plugin);
    void shutdown() throws Exception;

    Function<String, String> getStatementProcessor();

    Connection getConnection() throws Exception;
}
