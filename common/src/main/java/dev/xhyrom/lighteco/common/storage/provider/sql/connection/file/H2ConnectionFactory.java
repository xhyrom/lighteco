package dev.xhyrom.lighteco.common.storage.provider.sql.connection.file;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.sql.SqlImplementation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Function;

public class H2ConnectionFactory extends FileConnectionFactory {
    private Constructor<?> connectionConstructor;

    public H2ConnectionFactory(Path file) {
        super(file);
    }

    @Override
    public SqlImplementation getImplementationName() {
        return SqlImplementation.H2;
    }

    @Override
    public void init(LightEcoPlugin plugin) {
        // TODO: implement
        //ClassLoader classLoader = plugin
    }

    @Override
    protected Connection createConnection(Path file) throws SQLException {
        try {
            return (Connection) this.connectionConstructor.newInstance(
                    "jdbc:h2:" + file,
                    new Properties(),
                    null, null, false
            );
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }

            throw new SQLException("Failed to create connection", e);
        }
    }
}
