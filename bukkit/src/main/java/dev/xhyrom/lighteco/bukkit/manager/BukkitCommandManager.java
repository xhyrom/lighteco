package dev.xhyrom.lighteco.bukkit.manager;

import dev.jorel.commandapi.CommandAPICommand;
import dev.xhyrom.lighteco.common.manager.command.AbstractCommandManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitCommandManager extends AbstractCommandManager {
    public BukkitCommandManager(LightEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerCurrencyCommand(@NonNull Currency currency) {
        new CommandAPICommand(currency.getIdentifier())
                .withPermission("lighteco.command.balance." + currency.getIdentifier())
                .executesPlayer((sender, args) -> {
                    User user = this.plugin.getUserManager().getIfLoaded(sender.getUniqueId());

                    this.onBalance(user, currency);
                })
                .register();
    }
}
