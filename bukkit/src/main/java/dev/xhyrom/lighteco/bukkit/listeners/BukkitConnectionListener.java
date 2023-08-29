package dev.xhyrom.lighteco.bukkit.listeners;

import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import dev.xhyrom.lighteco.common.model.user.User;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class BukkitConnectionListener implements Listener {
    private final BukkitLightEcoPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BukkitConnectionListener(BukkitLightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        try {
            this.plugin.getStorage().loadUser(event.getUniqueId(), event.getName()).join();
        } catch (Exception e) {
            this.plugin.getBootstrap().getLogger()
                    .error("Failed to load user data for %s (%s)", e, event.getName(), event.getUniqueId());

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, miniMessage.deserialize(
                    "<bold>LightEco</bold> <red>Failed to load your data. Contact a staff member for assistance."
            ));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();

        User user = this.plugin.getUserManager().getIfLoaded(uniqueId);
        if (!user.isDirty()) {
            this.plugin.getUserManager().unload(uniqueId);
            return;
        }

        this.plugin.getUserManager().saveUser(user)
                .thenAccept(v -> {
                    // make sure the player is offline before unloading
                    if (Bukkit.getPlayer(uniqueId) != null) return;

                    this.plugin.getUserManager().unload(uniqueId);
                });
    }
}
