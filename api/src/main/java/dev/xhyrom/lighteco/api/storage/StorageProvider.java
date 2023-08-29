package dev.xhyrom.lighteco.api.storage;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.UUID;

public interface StorageProvider {
    /**
     * Initialize the storage provider.
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * Shutdown the storage provider.
     *
     * @throws Exception
     */
    void shutdown() throws Exception;

    @NonNull User loadUser(@NonNull UUID uniqueId, @Nullable String username) throws Exception;
    void saveUser(@NonNull User user) throws Exception;
    void saveUsers(@NonNull User... users) throws Exception;

    @NonNull List<User> getTopUsers(Currency currency, int length) throws Exception;
}
