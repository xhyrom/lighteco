package dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import dev.xhyrom.lighteco.common.config.storage.StorageDataConfig;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public abstract class DriverBasedHikariConnectionFactory extends HikariConnectionFactory {
    protected DriverBasedHikariConnectionFactory(StorageDataConfig configuration) {
        super(configuration);
    }

    protected abstract String driverClassName();

    protected abstract String driverJdbcIdentifier();

    @Override
    protected void configureDatabase(HikariConfig config, String address, String port, String databaseName, String username, String password) {
        config.setDriverClassName(driverClassName());
        config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", driverJdbcIdentifier(), address, port, databaseName));
        config.setUsername(username);
        config.setPassword(password);
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();

        deregisterDriver(driverClassName());
    }

    private static void deregisterDriver(String driverClassName) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getName().equals(driverClassName)) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {}
            }
        }
    }
}
