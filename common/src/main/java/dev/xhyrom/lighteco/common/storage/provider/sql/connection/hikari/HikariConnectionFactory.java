package dev.xhyrom.lighteco.common.storage.provider.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.xhyrom.lighteco.common.config.storage.StorageDataConfig;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class HikariConnectionFactory implements ConnectionFactory {
    private final StorageDataConfig configuration;
    private HikariDataSource hikari;

    protected HikariConnectionFactory(StorageDataConfig configuration) {
        this.configuration = configuration;
    }

    protected abstract String defaultPort();

    protected abstract void configureDatabase(HikariConfig config, String address, String port, String databaseName, String username, String password);

    protected void overrideProperties(Map<String, Object> properties) {
        // https://github.com/brettwooldridge/HikariCP/wiki/Rapid-Recovery
        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));
    }

    protected void setProperties(HikariConfig config, Map<String, Object> properties) {
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            config.addDataSourceProperty(property.getKey(), property.getValue());
        }
    }

    /**
     * Called after the Hikari pool has been initialised
     */
    protected void postInitialize() {

    }

    @Override
    public void init(LightEcoPlugin plugin) {
        HikariConfig config = new HikariConfig();

        // set pool name so the logging output can be linked back to us
        config.setPoolName("lighteco-hikari");

        // get the database info/credentials from the config file
        String[] addressSplit = this.configuration.address.split(":");
        String address = addressSplit[0];
        String port = addressSplit.length > 1 ? addressSplit[1] : defaultPort();

        // allow the implementation to configure the HikariConfig appropriately with these values
        configureDatabase(config, address, port, this.configuration.database, this.configuration.username, this.configuration.password);

        Map<String, Object> properties = new HashMap<>();

        this.overrideProperties(properties);
        this.setProperties(config, properties);

        // configure the connection pool
//        config.setMaximumPoolSize(1);
//        config.setMinimumIdle(10);
//        config.setMaxLifetime(1800000);
//        config.setKeepaliveTime(0);
//        config.setConnectionTimeout(5000);
//        config.setInitializationFailTimeout(-1);

        this.hikari = new HikariDataSource(config);

        postInitialize();
    }

    @Override
    public void shutdown() {
        if (this.hikari != null) {
            this.hikari.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.hikari == null) {
            throw new SQLException("Unable to get a connection from the pool. (hikari is null)");
        }

        Connection connection = this.hikari.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to get a connection from the pool. (getConnection returned null)");
        }

        return connection;
    }

    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace('\'', '`');
    }
}