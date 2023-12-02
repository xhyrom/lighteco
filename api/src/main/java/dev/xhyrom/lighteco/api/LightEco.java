package dev.xhyrom.lighteco.api;

import dev.xhyrom.lighteco.api.manager.CommandManager;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.manager.UserManager;
import dev.xhyrom.lighteco.api.messaging.MessagingService;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.api.platform.PlayerAdapter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public interface LightEco {
    /**
     * Gets the {@link Platform}, which represents the current platform the
     * plugin is running on.
     *
     * @return the platform
     */
    @NonNull Platform getPlatform();

    /**
     * Gets the {@link UserManager}, which manages the users.
     *
     * @return the user manager
     */
    @NonNull UserManager getUserManager();

    /**
     * Gets the {@link CurrencyManager}, which manages the currencies.
     *
     * @return the currency manager
     */
    @NonNull CurrencyManager getCurrencyManager();

    /**
     * Gets the {@link CommandManager}, which manages the commands.
     *
     * @return the command manager
     */
    @NonNull CommandManager getCommandManager();

    /**
     * Gets the {@link MessagingService}, which manages the messaging.
     *
     * @return the messaging service
     */
    @NonNull Optional<MessagingService> getMessagingService();

    /**
     * Gets the {@link PlayerAdapter} for a player class.
     *
     * @param playerClass the player class
     * @return the player adapter
     * @param <T> the player class
     */
    <T> @NonNull PlayerAdapter<T> getPlayerAdapter(@NonNull Class<T> playerClass);
}
