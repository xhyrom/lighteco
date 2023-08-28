package dev.xhyrom.lighteco.common.storage.provider.sql.connection.file;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Function;

public class SqliteConnectionFactory extends FileConnectionFactory {
    private Constructor<?> connectionConstructor;

    public SqliteConnectionFactory(File file) {
        super(file);
    }

    @Override
    public void init(LightEcoPlugin plugin) {
        // TODO: implement
    }

    @Override
    protected Connection createConnection(File file) throws SQLException {
        try {
            return (Connection) this.connectionConstructor.newInstance(
                    "jdbc:sqlite:" + file.getAbsolutePath(),
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
