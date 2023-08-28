package dev.xhyrom.lighteco.common.storage.provider.sql.connection;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.StorageType;

import java.sql.Connection;
import java.util.function.Function;

public interface ConnectionFactory {
    StorageType getImplementationName();

    void init(LightEcoPlugin plugin);
    void shutdown() throws Exception;

    Function<String, String> getStatementProcessor();

    Connection getConnection() throws Exception;
}
