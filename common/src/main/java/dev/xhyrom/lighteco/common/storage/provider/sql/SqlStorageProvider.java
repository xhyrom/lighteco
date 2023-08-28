package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.function.Function;

public class SqlStorageProvider implements StorageProvider {
    private static final String SAVE_USER_LOCAL_CURRENCY = "";
    private static final String SAVE_USER_GLOBAL_CURRENCY = "";

    private static final String LOAD_WHOLE_USER = """
    SELECT currency_identifier, balance
    FROM
        (
            SELECT currency_identifier, balance
            FROM {prefix}_users
            WHERE uuid=?1
            UNION ALL
            SELECT currency_identifier, balance
            FROM {prefix}_{context}_users
            WHERE uuid=?1
        );
    """;

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
    }

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId) throws Exception {
        String uniqueIdString = uniqueId.toString();
        dev.xhyrom.lighteco.common.model.user.User user = this.plugin.getUserManager().getOrMake(uniqueId);

        try (Connection c = this.connectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(LOAD_WHOLE_USER)) {
                ps.setString(1, uniqueIdString);
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
    public void saveUser(@NonNull User user) {

    }
}
