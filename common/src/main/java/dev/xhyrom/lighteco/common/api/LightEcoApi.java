package dev.xhyrom.lighteco.common.api;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.managers.UserManager;
import dev.xhyrom.lighteco.api.platform.Platform;
import dev.xhyrom.lighteco.api.platform.PlayerAdapter;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.api.impl.ApiPlatform;
import dev.xhyrom.lighteco.common.api.impl.ApiPlayerAdapter;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LightEcoApi implements LightEco {
    private final LightEcoPlugin plugin;

    private final Platform platform;
    private final PlayerAdapter<?> playerAdapter;

    public LightEcoApi(LightEcoPlugin plugin) {
        this.plugin = plugin;

        this.platform = new ApiPlatform(plugin);
        this.playerAdapter = new ApiPlayerAdapter<>(plugin.getUserManager(), plugin.getContextManager());
    }

    @Override
    public @NonNull Platform getPlatform() {
        return this.platform;
    }

    @Override
    public @NonNull UserManager getUserManager() {
        return this.plugin.getUserManager();
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
