package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.util.UUID;

public class SqlStorageProvider implements StorageProvider {
    private final LightEcoPlugin plugin;
    private final ConnectionFactory connectionFactory;

    public SqlStorageProvider(LightEcoPlugin plugin, ConnectionFactory connectionFactory) {
        this.plugin = plugin;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public @NonNull User loadUser(@NonNull UUID uniqueId) throws Exception {
        try (Connection c = this.connectionFactory.getConnection()) {

        }

        return null;
    }

    @Override
    public void saveUser(@NonNull User user) {

    }
}
