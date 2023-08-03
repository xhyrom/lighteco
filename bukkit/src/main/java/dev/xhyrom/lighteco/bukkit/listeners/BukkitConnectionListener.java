package dev.xhyrom.lighteco.bukkit.listeners;

import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            this.plugin.getStorage().loadUser(event.getUniqueId()).join();
        } catch (Exception e) {
            this.plugin.getBootstrap().getLogger()
                    .error("Failed to load user data for %s (%s)", e, event.getName(), event.getUniqueId());

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, miniMessage.deserialize(
                    "<bold>LightEco</bold> <red>Failed to load your data. Please try again later."
            ));
        }
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        this.plugin.getUserManager().unload(event.getPlayer().getUniqueId());
    }
}
