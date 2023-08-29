package dev.xhyrom.lighteco.api.manager;

import dev.xhyrom.lighteco.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager {
    @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId);
    @NonNull CompletableFuture<User> loadUser(@NonNull UUID uniqueId, String username);

    @NonNull CompletableFuture<Void> saveUser(@NonNull User user);

    /**
     * Saves multiple users at once.
     * <p>
     *     Uses TRANSACTION clause under the hood.
     *     If one of the users fails to save, the entire transaction will be rolled back.
     *     This is to ensure data integrity.
     *     If you want to save users individually, use {@link #saveUser(User)} instead.
     * </p>
     *
     * @param users the users to save
     * @return a future that completes when the users have been saved
     */
    @NonNull CompletableFuture<Void> saveUsers(@NonNull User... users);

    @Nullable User getUser(@NonNull UUID uniqueId);

    boolean isLoaded(@NonNull UUID uniqueId);
}
