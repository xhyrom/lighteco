package dev.xhyrom.lighteco.bukkit.managers;

import dev.xhyrom.lighteco.api.managers.ContextManager;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class BukkitContextManager implements ContextManager<Player> {
    @Override
    public @NonNull UUID getPlayerUniqueId(@NonNull Player player) {
        return player.getUniqueId();
    }

    @Override
    public @NonNull Class<?> getPlayerClass() {
        return Player.class;
    }
}
