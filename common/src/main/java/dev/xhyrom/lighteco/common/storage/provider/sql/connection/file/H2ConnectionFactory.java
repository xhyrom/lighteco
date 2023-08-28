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

public class H2ConnectionFactory extends FileConnectionFactory {
    private Constructor<?> connectionConstructor;

    public H2ConnectionFactory(Path file) {
        super(file);
    }

    @Override
    public StorageType getImplementationName() {
        return StorageType.H2;
    }

    @Override
    public void init(LightEcoPlugin plugin) {
        ClassLoader classLoader = plugin.getDependencyManager().obtainClassLoaderWith(EnumSet.of(Dependency.H2_DRIVER));

        try {
            Class<?> connectionClass = classLoader.loadClass("org.h2.jdbc.JdbcConnection");
            this.connectionConstructor = connectionClass.getConstructor(String.class, Properties.class, String.class, Object.class, boolean.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection createConnection(Path file) throws SQLException {
        try {
            return (Connection) this.connectionConstructor.newInstance(
                    "jdbc:h2:" + file.toString() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE",
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

    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s
                .replace('\'', '`')
                .replace("LIKE", "ILIKE")
                .replace("value", "`value`")
                .replace("``value``", "`value`");
    }
}
