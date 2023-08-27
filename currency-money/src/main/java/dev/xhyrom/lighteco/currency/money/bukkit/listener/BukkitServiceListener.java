package dev.xhyrom.lighteco.currency.money.bukkit.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;

public class BukkitServiceListener implements Listener {
    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (!(event.getProvider().getProvider() instanceof Economy)) return;


    }
}
