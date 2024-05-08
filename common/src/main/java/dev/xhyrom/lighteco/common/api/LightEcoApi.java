package dev.xhyrom.lighteco.common.api;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.manager.CommandManager;
import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.manager.UserManager;
import dev.xhyrom.lighteco.api.messaging.MessagingService;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.api.platform.PlayerAdapter;
import dev.xhyrom.lighteco.common.api.impl.*;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class LightEcoApi implements LightEco {
    private final LightEcoPlugin plugin;

    private final Platform platform;
    private final UserManager userManager;
    private final CurrencyManager currencyManager;
    private final CommandManager commandManager;
    private final PlayerAdapter<?> playerAdapter;

    public LightEcoApi(LightEcoPlugin plugin) {
        this.plugin = plugin;

        this.platform = new ApiPlatform(plugin);
        this.userManager = new ApiUserManager(plugin, plugin.getUserManager());
        this.currencyManager = new ApiCurrencyManager(plugin, plugin.getCurrencyManager());
        this.commandManager = new ApiCommandManager(plugin, plugin.getCommandManager());
        this.playerAdapter = new ApiPlayerAdapter<>(userManager, plugin.getContextManager());
    }

    @Override
    public @NonNull Platform getPlatform() {
        return this.platform;
    }

    @Override
    public @NonNull UserManager getUserManager() {
        return this.userManager;
    }

    @Override
    public @NonNull CurrencyManager getCurrencyManager() {
        return this.currencyManager;
    }

    @Override
    public @NonNull CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NonNull Optional<MessagingService> getMessagingService() {
        return this.plugin.getMessagingService().map(ApiMessagingService::new);
    }

    @Override
    public @NonNull <T> PlayerAdapter<T> getPlayerAdapter(@NonNull Class<T> playerClass) {
        Class<?> expected = this.plugin.getContextManager().getPlayerClass();

        if (!expected.equals(playerClass)) {
            throw new IllegalArgumentException("Expected player class " + expected.getName() + ", got " + playerClass.getName());
        }

        return (PlayerAdapter<T>) this.playerAdapter;
    }
}
