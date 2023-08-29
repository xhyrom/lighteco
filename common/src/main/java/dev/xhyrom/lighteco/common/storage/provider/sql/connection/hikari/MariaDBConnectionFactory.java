package dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari;

import dev.xhyrom.lighteco.common.config.storage.StorageDataConfig;
import dev.xhyrom.lighteco.common.storage.StorageType;

public class MariaDBConnectionFactory extends DriverBasedHikariConnectionFactory {
    public MariaDBConnectionFactory(StorageDataConfig configuration) {
        super(configuration);
    }

    @Override
    public StorageType getImplementationName() {
        return StorageType.MARIADB;
    }

    @Override
    protected String defaultPort() {
        return "3306";
    }

    @Override
    protected String driverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    protected String driverJdbcIdentifier() {
        return "mariadb";
    }
}
