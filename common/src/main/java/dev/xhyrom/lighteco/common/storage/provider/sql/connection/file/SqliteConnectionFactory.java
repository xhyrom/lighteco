package dev.xhyrom.lighteco.common.storage.provider.sql.connection.file;

import dev.xhyrom.lighteco.common.dependencies.Dependency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.StorageType;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Properties;
import java.util.function.Function;

public class SqliteConnectionFactory extends FileConnectionFactory {
    private Constructor<?> connectionConstructor;

    public SqliteConnectionFactory(Path file) {
        super(file);
    }

    @Override
    public StorageType getImplementationName() {
        return StorageType.SQLITE;
    }

    @Override
    public void init(LightEcoPlugin plugin) {
        ClassLoader classLoader = plugin.getDependencyManager()
                .obtainClassLoaderWith(EnumSet.of(Dependency.SQLITE_DRIVER));

        try {
            Class<?> connectionClass = classLoader.loadClass("org.sqlite.jdbc4.JDBC4Connection");
            this.connectionConstructor =
                    connectionClass.getConstructor(String.class, String.class, Properties.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection createConnection(Path file) throws SQLException {
        try {
            return (Connection) this.connectionConstructor.newInstance(
                    "jdbc:sqlite:" + file, file.toString(), new Properties());
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }

            throw new SQLException("Failed to create connection", e);
        }
    }

    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace('\'', '`');
    }
}
