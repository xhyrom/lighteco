package dev.xhyrom.lighteco.common.storage;

import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.util.ThrowableRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Storage {
    private final LightEcoPlugin plugin;
    private final StorageProvider provider;

    public Storage(LightEcoPlugin plugin, StorageProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
    }

    private <T> CompletableFuture<T> future(Callable<T> callable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException r) {
                    throw r;
                }

                throw new CompletionException(e);
            }
        });
    }

    private CompletableFuture<Void> future(ThrowableRunnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                if (e instanceof RuntimeException r) {
                    throw r;
                }

                throw new CompletionException(e);
            }
        });
    }

    public void init() {
        try {
            this.provider.init();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize storage provider", e);
        }
    }

    public void shutdown() {
        try {
            this.provider.shutdown();
        } catch (Exception e) {
            throw new RuntimeException("Failed to shutdown storage provider", e);
        }
    }

    public CompletableFuture<User> loadUser(UUID uniqueId) {
        return loadUser(uniqueId, null);
    }

    public CompletableFuture<User> loadUser(UUID uniqueId, String username) {
        User user = this.plugin.getUserManager().getIfLoaded(uniqueId);
        if (user != null) {
            return CompletableFuture.completedFuture(user);
        }

        return future(() -> this.provider.loadUser(uniqueId, username))
                .thenApply(apiUser -> this.plugin.getUserManager().getIfLoaded(apiUser.getUniqueId()));
    }

    public CompletableFuture<Void> saveUser(dev.xhyrom.lighteco.api.model.user.User user) {
        return future(() -> this.provider.saveUser(user));
    }

    public CompletableFuture<Void> saveUsers(dev.xhyrom.lighteco.api.model.user.User... users) {
        return future(() -> this.provider.saveUsers(users));
    }

    // Return ApiUser instead of User
    // We don't do anything with this
    public CompletableFuture<List<dev.xhyrom.lighteco.api.model.user.User>> getTopUsers(dev.xhyrom.lighteco.api.model.currency.Currency currency, int length) {
        return future(() -> this.provider.getTopUsers(currency, length));
    }
}
