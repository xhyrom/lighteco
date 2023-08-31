package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.StorageType;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class SqlStorageProvider implements StorageProvider {
    private static String SAVE_USER_LOCAL_CURRENCY;
    private static String SAVE_USER_GLOBAL_CURRENCY;

    private static String LOAD_LOCAL_CURRENCY_USER;
    private static String LOAD_GLOBAL_CRRENCY_USER;
    private static final String GET_TOP_X_USERS_LOCAL = "SELECT uuid, balance FROM {prefix}_local_{context}_{currency}_users ORDER BY balance DESC LIMIT ?;";
    private static final String GET_TOP_X_USERS_GLOBAL = "SELECT uuid, balance FROM {prefix}_global_{currency}_users ORDER BY balance DESC LIMIT ?;";

    private static final String DELETE_LOCAL_USER_IF_BALANCE = "DELETE FROM {prefix}_local_{context}_{currency}_users WHERE uuid = ? AND balance = ?;";
    private static final String DELETE_GLOBAL_USER_IF_BALANCE = "DELETE FROM {prefix}_global_{context}_{currency}_users WHERE uuid = ? AND balance = ?;";

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
        LOAD_LOCAL_CURRENCY_USER = SqlStatements.LOAD_LOCAL_CURRENCY_USER.get(implementationName);
        LOAD_GLOBAL_CRRENCY_USER = SqlStatements.LOAD_GLOBAL_CURRENCY_USER.get(implementationName);
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

        StringBuilder query = new StringBuilder();
        List<Currency> currencies = this.plugin.getCurrencyManager().getRegisteredCurrencies().stream().toList();
        int size = this.plugin.getCurrencyManager().getRegisteredCurrencies().size();

        for (int i = 0; i < size; i++) {
            Currency currency = currencies.get(i);

            switch (currency.getType()) {
                case GLOBAL -> query.append(
                        this.statementProcessor.apply(LOAD_GLOBAL_CRRENCY_USER.replace("{currency}", currency.getIdentifier()))
                );
                case LOCAL -> query.append(
                        this.statementProcessor.apply(LOAD_LOCAL_CURRENCY_USER.replace("{currency}", currency.getIdentifier()))
                );
            }

            if (i != size - 1) {
                query.append(" UNION ALL ");
            }
        }

        System.out.println(query.toString());

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(query.toString())) {
                if (SqlStatements.mustDuplicateParameters(this.connectionFactory.getImplementationName())) {
                    for (int i = 0; i < size; i++) {
                        ps.setString(i + 1, uniqueIdString);
                    }
                } else {
                    ps.setString(1, uniqueIdString);
                }

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String currencyIdentifier = rs.getString("name");
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
            try {
                saveBalances(c, user, uniqueIdString);
            } catch (SQLException e) {
                throw new SQLException("Failed to save user " + user.getUniqueId(), e);
            }
        }
    }

    @Override
    public void saveUsers(@NotNull @NonNull User... users) throws Exception {
        // use transaction
        try (Connection c = this.connectionFactory.getConnection()) {
            try {
                c.setAutoCommit(false);

                for (User user : users) {
                    String uniqueIdString = user.getUniqueId().toString();

                    saveBalances(c, user, uniqueIdString);
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    @Override
    public @NonNull List<User> getTopUsers(dev.xhyrom.lighteco.api.model.currency.Currency apiCurrency, int length) throws Exception {
        Currency currency = this.plugin.getCurrencyManager().getIfLoaded(apiCurrency.getIdentifier());
        String statement = currency.getType() == dev.xhyrom.lighteco.api.model.currency.Currency.Type.GLOBAL
                ? GET_TOP_X_USERS_GLOBAL
                : GET_TOP_X_USERS_LOCAL;

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                    this.statementProcessor.apply(statement.replace("{currency}", currency.getIdentifier()))
            )) {
                ps.setString(1, currency.getIdentifier());
                ps.setInt(2, length);

                ResultSet rs = ps.executeQuery();

                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    String uniqueIdString = rs.getString("uuid");
                    UUID uniqueId = UUID.fromString(uniqueIdString);

                    BigDecimal balance = rs.getBigDecimal("balance");

                    dev.xhyrom.lighteco.common.model.user.User user = this.plugin.getUserManager().getOrMake(uniqueId);
                    user.setBalance(currency, balance);

                    users.add(user.getProxy());
                }

                return users;
            }
        }
    }

    private void saveBalances(Connection c, User user, String uniqueIdString) throws SQLException {
        c.setAutoCommit(false);

        for (Currency currency : this.plugin.getCurrencyManager().getRegisteredCurrencies()) {
            BigDecimal balance = user.getBalance(currency.getProxy());

            if (balance.compareTo(BigDecimal.ZERO) == 0) {
                switch (currency.getType()) {
                    case GLOBAL -> {
                        try (PreparedStatement ps = c.prepareStatement(this.statementProcessor.apply(DELETE_GLOBAL_USER_IF_BALANCE))) {
                            ps.setString(1, uniqueIdString);
                            ps.setString(2, currency.getIdentifier());
                            ps.setBigDecimal(3, balance);
                        }
                    }
                    case LOCAL -> {
                        psDeleteLocal.setString(1, uniqueIdString);
                        psDeleteLocal.setString(2, currency.getIdentifier());
                        psDeleteLocal.setBigDecimal(3, balance);

                        psDeleteLocal.addBatch();
                    }
                }

                continue;
            }

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
    }
}
