package dev.xhyrom.lighteco.paper.manager;

import dev.xhyrom.lighteco.api.manager.ContextManager;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class PaperContextManager implements ContextManager<Player> {
    @Override
    public @NonNull UUID getPlayerUniqueId(@NonNull Player player) {
        return player.getUniqueId();
    }

    @Override
    public @NonNull Class<?> getPlayerClass() {
        return Player.class;
    }
}
