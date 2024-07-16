package dev.xhyrom.lighteco.paper.listeners;

import dev.xhyrom.lighteco.paper.PaperLightEcoPlugin;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PaperConnectionListener implements Listener {
    private final PaperLightEcoPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public PaperConnectionListener(PaperLightEcoPlugin plugin) {
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

            Component reason = miniMessage.deserialize(
                    "<bold>LightEco</bold> <red>Failed to load your data. Contact a staff member for assistance."
            );

            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    LegacyComponentSerializer.legacySection().serialize(
                            reason
                    )
            );
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
                .thenAccept(v -> this.plugin.getMessagingService().ifPresent(service -> {
                    for (Currency currency : this.plugin.getCurrencyManager().getRegisteredCurrencies()) {
                        service.pushUserUpdate(user, currency);
                    }
                }));
    }
}
