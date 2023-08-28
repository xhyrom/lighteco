package dev.xhyrom.lighteco.common.storage.provider.sql.connection.file;

import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnectionFactory extends FileConnectionFactory {
    private Constructor<?> connectionConstructor;

    public SqliteConnectionFactory(Path file) {
        super(file);
    }

    @Override
    public String getImplementationName() {
        return "sqlite";
    }

    @Override
    public void init(LightEcoPlugin plugin) {
        /*ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            Class<?> connectionClass = classLoader.loadClass("org.sqlite.jdbc4.JDBC4Connection");
            this.connectionConstructor = connectionClass.getConstructor(String.class, String.class, Properties.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Override
    protected Connection createConnection(Path file) throws SQLException {
        try {
            /*return (Connection) this.connectionConstructor.newInstance(
                    "jdbc:sqlite:" + file,
                    new Properties(),
                    null, null, false
            );*/
            return DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }

            throw new SQLException("Failed to create connection", e);
        }
    }
}
