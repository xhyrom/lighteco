package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.StorageType;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class SqlStorageProvider implements StorageProvider {
    private static String SAVE_USER_LOCAL_CURRENCY;
    private static String SAVE_USER_GLOBAL_CURRENCY;

    private static String LOAD_LOCAL_CURRENCY_USER;
    private static String LOAD_GLOBAL_CRRENCY_USER;

    private static final String DELETE_LOCAL_USER =
            "DELETE FROM {prefix}_local_{context}_{currency}_users WHERE uuid = ?;";
    private static final String DELETE_GLOBAL_USER =
            "DELETE FROM {prefix}_global_{currency}_users WHERE uuid = ?;";
    private static final String CREATE_TABLE =
            """
    CREATE TABLE IF NOT EXISTS '{prefix}_{table}' (
        'uuid'                  VARCHAR(36)     NOT NULL,
        'balance'               DECIMAL(20, 2)  NOT NULL,
        PRIMARY KEY (`uuid`)
    );
    """
                    .trim();

    private final LightEcoPlugin plugin;
    private final ConnectionFactory connectionFactory;
    private final Function<String, String> statementProcessor;

    public SqlStorageProvider(LightEcoPlugin plugin, ConnectionFactory connectionFactory) {
        this.plugin = plugin;
        this.connectionFactory = connectionFactory;
        this.statementProcessor = connectionFactory.getStatementProcessor().compose(s -> s.replace(
                        "{prefix}", plugin.getConfig().storage.tablePrefix)
                .replace("{context}", plugin.getConfig().server));

        final StorageType implementationName = this.connectionFactory.getImplementationName();
        SAVE_USER_LOCAL_CURRENCY = SqlStatements.SAVE_USER_LOCAL_CURRENCY.get(implementationName);
        SAVE_USER_GLOBAL_CURRENCY = SqlStatements.SAVE_USER_GLOBAL_CURRENCY.get(implementationName);
        LOAD_LOCAL_CURRENCY_USER = SqlStatements.LOAD_LOCAL_CURRENCY_USER.get(implementationName);
        LOAD_GLOBAL_CRRENCY_USER = SqlStatements.LOAD_GLOBAL_CURRENCY_USER.get(implementationName);
    }

    @Override
    public void init() {
        this.connectionFactory.init(this.plugin);
    }

    @Override
    public void shutdown() throws Exception {
        this.connectionFactory.shutdown();
    }

    @Override
    public void registerCurrency(dev.xhyrom.lighteco.api.model.currency.@NonNull Currency currency)
            throws Exception {
        StringBuilder tableName = new StringBuilder();

        if (currency.getType() == dev.xhyrom.lighteco.api.model.currency.Currency.Type.LOCAL) {
            tableName.append("local_{context}_");
            tableName.append(currency.getIdentifier());
        } else {
            tableName.append("global_");
            tableName.append(currency.getIdentifier());
        }

        tableName.append("_users");

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(this.statementProcessor.apply(
                    CREATE_TABLE.replace("{table}", tableName.toString())))) {
                ps.execute();
            }
        }
    }

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId, @Nullable String username)
            throws Exception {
        String uniqueIdString = uniqueId.toString();
        dev.xhyrom.lighteco.common.model.user.User user =
                this.plugin.getUserManager().getOrMake(uniqueId);
        if (username != null) user.setUsername(username);

        StringBuilder query = new StringBuilder();
        List<Currency> currencies =
                this.plugin.getCurrencyManager().getRegisteredCurrencies().stream()
                        .toList();
        int size = this.plugin.getCurrencyManager().getRegisteredCurrencies().size();

        for (int i = 0; i < size; i++) {
            Currency currency = currencies.get(i);

            switch (currency.getType()) {
                case GLOBAL -> query.append(this.statementProcessor
                        .apply(LOAD_GLOBAL_CRRENCY_USER.replace(
                                "{currency}", currency.getIdentifier()))
                        .replace("{identifier}", "'" + currency.getIdentifier() + "'"));
                case LOCAL -> query.append(this.statementProcessor
                        .apply(LOAD_LOCAL_CURRENCY_USER.replace(
                                "{currency}", currency.getIdentifier()))
                        .replace("{identifier}", "'" + currency.getIdentifier() + "'"));
            }

            if (i != size - 1) {
                query.append(" UNION ALL ");
            }
        }

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(query.toString())) {
                if (SqlStatements.mustDuplicateParameters(
                        this.connectionFactory.getImplementationName())) {
                    for (int i = 0; i < size; i++) {
                        ps.setString(i + 1, uniqueIdString);
                    }
                } else {
                    ps.setString(1, uniqueIdString);
                }

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String identifier = rs.getString("name");
                    Currency currency = this.plugin.getCurrencyManager().getIfLoaded(identifier);

                    BigDecimal balance = rs.getBigDecimal("balance");

                    user.setBalance(currency, balance, true);
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
    public void saveUsers(@NonNull User... users) throws Exception {
        // use transaction
        try (Connection c = this.connectionFactory.getConnection()) {
            try {
                c.setAutoCommit(false);

                for (User user : users) {
                    String uniqueIdString = user.getUniqueId().toString();

                    saveBalances(c, user, uniqueIdString, false);
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private void saveBalances(Connection c, User user, String uniqueIdString) throws SQLException {
        saveBalances(c, user, uniqueIdString, true);
    }

    private void saveBalances(Connection c, User user, String uniqueIdString, boolean transactions)
            throws SQLException {
        if (transactions) c.setAutoCommit(false);

        for (Currency currency : this.plugin.getCurrencyManager().getRegisteredCurrencies()) {
            BigDecimal balance = user.getBalance(currency.getProxy());

            if (balance.compareTo(BigDecimal.ZERO) == 0) {
                switch (currency.getType()) {
                    case GLOBAL -> {
                        try (PreparedStatement ps = c.prepareStatement(
                                this.statementProcessor.apply(DELETE_GLOBAL_USER.replace(
                                        "{currency}", currency.getIdentifier())))) {
                            ps.setString(1, uniqueIdString);

                            ps.execute();
                        }
                    }
                    case LOCAL -> {
                        try (PreparedStatement ps = c.prepareStatement(
                                this.statementProcessor.apply(DELETE_LOCAL_USER.replace(
                                        "{currency}", currency.getIdentifier())))) {
                            ps.setString(1, uniqueIdString);

                            ps.execute();
                        }
                    }
                }

                continue;
            }

            switch (currency.getType()) {
                case GLOBAL -> {
                    try (PreparedStatement ps = c.prepareStatement(
                            this.statementProcessor.apply(SAVE_USER_GLOBAL_CURRENCY.replace(
                                    "{currency}", currency.getIdentifier())))) {
                        ps.setString(1, uniqueIdString);
                        ps.setBigDecimal(2, balance);
                        if (SqlStatements.mustDuplicateParameters(
                                this.connectionFactory.getImplementationName()))
                            ps.setBigDecimal(3, balance);

                        ps.execute();
                    }
                }
                case LOCAL -> {
                    try (PreparedStatement psLocal = c.prepareStatement(
                            this.statementProcessor.apply(SAVE_USER_LOCAL_CURRENCY.replace(
                                    "{currency}", currency.getIdentifier())))) {
                        psLocal.setString(1, uniqueIdString);
                        psLocal.setBigDecimal(2, balance);
                        if (SqlStatements.mustDuplicateParameters(
                                this.connectionFactory.getImplementationName()))
                            psLocal.setBigDecimal(3, balance);

                        psLocal.execute();
                    }
                }
            }
        }

        if (transactions) {
            try {
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw new SQLException("Failed to save user " + user.getUniqueId(), e);
            }
        }
    }
}
