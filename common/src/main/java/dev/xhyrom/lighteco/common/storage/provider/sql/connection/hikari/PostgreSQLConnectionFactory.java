package dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari;

import dev.xhyrom.lighteco.common.config.storage.StorageDataConfig;
import dev.xhyrom.lighteco.common.storage.StorageType;

import java.util.Map;
import java.util.function.Function;

public class PostgreSQLConnectionFactory extends DriverBasedHikariConnectionFactory {
    public PostgreSQLConnectionFactory(StorageDataConfig configuration) {
        super(configuration);
    }

    @Override
    public StorageType getImplementationName() {
        return StorageType.POSTGRESQL;
    }

    @Override
    protected String defaultPort() {
        return "5432";
    }

    @Override
    protected String driverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    protected String driverJdbcIdentifier() {
        return "postgresql";
    }

    @Override
    protected void overrideProperties(Map<String, Object> properties) {
        super.overrideProperties(properties);

        // Doesn't work with PostgreSQL
        properties.remove("useUnicode");
        properties.remove("characterEncoding");
    }

    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace("\'", "\"");
    }
}
