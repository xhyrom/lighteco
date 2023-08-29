package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.StorageType;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class SqlStorageProvider implements StorageProvider {
    private static String SAVE_USER_LOCAL_CURRENCY;
    private static String SAVE_USER_GLOBAL_CURRENCY;
    private static String LOAD_WHOLE_USER;
    
    private final LightEcoPlugin plugin;
    private final ConnectionFactory connectionFactory;
    private final Function<String, String> statementProcessor;

    public SqlStorageProvider(LightEcoPlugin plugin, ConnectionFactory connectionFactory) {
        this.plugin = plugin;
        this.connectionFactory = connectionFactory;
        this.statementProcessor = connectionFactory.getStatementProcessor().compose(
                s -> s
                        .replace("{prefix}", plugin.getConfig().storage.tablePrefix)
                        .replace("{context}", plugin.getConfig().server)
        );

        final StorageType implementationName = this.connectionFactory.getImplementationName();
        SAVE_USER_LOCAL_CURRENCY = SqlStatements.SAVE_USER_LOCAL_CURRENCY.get(implementationName);
        SAVE_USER_GLOBAL_CURRENCY = SqlStatements.SAVE_USER_GLOBAL_CURRENCY.get(implementationName);
        LOAD_WHOLE_USER = SqlStatements.LOAD_WHOLE_USER.get(implementationName);
    }

    @Override
    public void init() throws Exception {
        this.connectionFactory.init(this.plugin);

        List<String> statements;
        String schemaFileName = "schema/" + this.connectionFactory.getImplementationName().name().toLowerCase() + ".sql";
        try (InputStream is = this.plugin.getBootstrap().getResourceStream(schemaFileName)) {
            if (is == null)
                throw new IOException("Failed to load schema file: " + schemaFileName);

            statements = SchemaReader.getStatements(is).stream()
                    .map(this.statementProcessor)
                    .toList();
        }

        try (Connection c = this.connectionFactory.getConnection()) {
            try (Statement s = c.createStatement()) {
                for (String statement : statements) {
                    s.addBatch(statement);
                }

                s.executeBatch();
            }
        }
    }

    @Override
    public void shutdown() throws Exception {
        this.connectionFactory.shutdown();
    }

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId, @Nullable String username) throws Exception {
        String uniqueIdString = uniqueId.toString();
        dev.xhyrom.lighteco.common.model.user.User user = this.plugin.getUserManager().getOrMake(uniqueId);
        if (username != null)
            user.setUsername(username);

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(this.statementProcessor.apply(LOAD_WHOLE_USER))) {
                ps.setString(1, uniqueIdString);
                if (SqlStatements.mustDuplicateParameters(this.connectionFactory.getImplementationName()))
                    ps.setString(2, uniqueIdString);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String currencyIdentifier = rs.getString("currency_identifier");
                    Currency currency = this.plugin.getCurrencyManager().getIfLoaded(currencyIdentifier);

                    BigDecimal balance = rs.getBigDecimal("balance");

                    user.setBalance(currency, balance);
                }
            }
        }

        return user.getProxy();
    }

    @Override
    public void saveUser(@NonNull User user) throws Exception {
        String uniqueIdString = user.getUniqueId().toString();

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement psGlobal = c.prepareStatement(this.statementProcessor.apply(SAVE_USER_GLOBAL_CURRENCY));
                 PreparedStatement psLocal = c.prepareStatement(this.statementProcessor.apply(SAVE_USER_LOCAL_CURRENCY))) {

                for (Currency currency : this.plugin.getCurrencyManager().getRegisteredCurrencies()) {
                    BigDecimal balance = user.getBalance(currency.getProxy());

                    if (balance.compareTo(BigDecimal.ZERO) == 0) continue;

                    switch (currency.getType()) {
                        case GLOBAL -> {
                            psGlobal.setString(1, uniqueIdString);
                            psGlobal.setString(2, currency.getIdentifier());
                            psGlobal.setBigDecimal(3, balance);
                            if (SqlStatements.mustDuplicateParameters(this.connectionFactory.getImplementationName()))
                                psGlobal.setBigDecimal(4, balance);

                            psGlobal.addBatch();
                        }
                        case LOCAL -> {
                            psLocal.setString(1, uniqueIdString);
                            psLocal.setString(2, currency.getIdentifier());
                            psLocal.setBigDecimal(3, balance);
                            if (SqlStatements.mustDuplicateParameters(this.connectionFactory.getImplementationName()))
                                psLocal.setBigDecimal(4, balance);

                            psLocal.addBatch();
                        }
                    }
                }

                psGlobal.executeBatch();
                psLocal.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save user " + user.getUniqueId(), e);
            }
        }
    }
}
