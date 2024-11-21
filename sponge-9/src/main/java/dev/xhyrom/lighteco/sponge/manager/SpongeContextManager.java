package dev.xhyrom.lighteco.sponge.manager;

import dev.xhyrom.lighteco.api.manager.ContextManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class SpongeContextManager implements ContextManager<Player> {
    @Override
    public @NonNull UUID getPlayerUniqueId(@NonNull Player player) {
        return player.uniqueId();
    }

    @Override
    public @NonNull Class<?> getPlayerClass() {
        return Player.class;
    }
}
