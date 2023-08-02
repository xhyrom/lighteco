package dev.xhyrom.lighteco.bukkit.listeners;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitConnectionListener implements Listener {
    private final BukkitLightEcoPlugin plugin;
    public BukkitConnectionListener(BukkitLightEcoPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        try {
            User user = this.plugin.getStorage().loadUser(event.getUniqueId()).join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        // house keeper stuff

        // for now:
        this.plugin.getUserManager().unload(event.getPlayer().getUniqueId());
    }
}
