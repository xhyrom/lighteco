package dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.StorageType;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;

import java.sql.Connection;
import java.util.function.Function;

public class HikariConnectionFactory implements ConnectionFactory {

    @Override
    public StorageType getImplementationName() {
        return null;
    }

    @Override
    public void init(LightEcoPlugin plugin) {

    }

    @Override
    public void shutdown() throws Exception {

    }

    @Override
    public Function<String, String> getStatementProcessor() {
        return null;
    }

    @Override
    public Connection getConnection() throws Exception {
        return null;
    }
}
