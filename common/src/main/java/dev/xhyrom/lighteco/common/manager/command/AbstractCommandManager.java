package dev.xhyrom.lighteco.common.manager.command;

import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommandManager implements CommandManager {
    protected final LightEcoPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final ArrayList<UUID> mustWait = new ArrayList<>();

    public AbstractCommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canUse(CommandSender sender) {
        // Console doesn't need to wait
        if (sender.getUniqueId() == null) return true;

        if (mustWait.contains(sender.getUniqueId())) {
            sender.sendMessage(
                    miniMessage.deserialize("<red>Please wait a moment before using this command again.")
            );

            return false;
        }

        return true;
    }

    private void addToMustWait(UUID ...uuids) {
        for (UUID uuid : uuids) {
            if (uuid != null)
                mustWait.add(uuid);
        }
    }

    private void removeFromMustWait(UUID ...uuids) {
        for (UUID uuid : uuids) {
            if (uuid != null)
                mustWait.remove(uuid);
        }
    }

    @Override
    public void onBalance(CommandSender sender, Currency currency) {
        User user = this.plugin.getUserManager().getIfLoaded(sender.getUniqueId());
        BigDecimal balance = user.getBalance(currency);

        sender.sendMessage(
                miniMessage.deserialize("<yellow>Your balance: <gold>" + balance.toPlainString() + " <yellow>" + currency.getIdentifier())
        );
    }

    @Override
    public void onBalance(CommandSender sender, Currency currency, User target) {
        BigDecimal balance = target.getBalance(currency);

        sender.sendMessage(
                miniMessage.deserialize("<yellow>" + target.getUsername() + "'s balance: <gold>" + balance.toPlainString() + " <yellow>" + currency.getIdentifier())
        );
    }

    @Override
    public void onSet(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());

        target.setBalance(currency, amount);

        sender.sendMessage(
                miniMessage.deserialize("<yellow>Set " + target.getUsername() + "'s balance to <gold>" + amount.toPlainString() + " <yellow>" + currency.getIdentifier())
        );

        this.plugin.getUserManager().saveUser(target)
                .thenAccept(v -> removeFromMustWait(target.getUniqueId(), sender.getUniqueId()));
    }

    @Override
    public void onGive(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());

        target.setBalance(currency, target.getBalance(currency).add(amount));

        sender.sendMessage(
                miniMessage.deserialize("<yellow>Gave " + target.getUsername() + " <gold>" + amount.toPlainString() + " <yellow>" + currency.getIdentifier())
        );

        this.plugin.getUserManager().saveUser(target)
                .thenAccept(v -> removeFromMustWait(target.getUniqueId(), sender.getUniqueId()));
    }

    @Override
    public void onTake(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());

        target.setBalance(currency, target.getBalance(currency).subtract(amount));

        sender.sendMessage(
                miniMessage.deserialize("<yellow>Took <gold>" + amount.toPlainString() + " <yellow>" + currency.getIdentifier() + " from " + target.getUsername())
        );

        this.plugin.getUserManager().saveUser(target)
                .thenAccept(v -> removeFromMustWait(target.getUniqueId(), sender.getUniqueId()));
    }

    @Override
    public void onPay(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());

        User user = this.plugin.getUserManager().getIfLoaded(sender.getUniqueId());

        // calculate tax using Currency#calculateTax
        BigDecimal tax = currency.getProxy().calculateTax(user.getProxy(), amount);

        // subtract tax from amount
        BigDecimal taxedAmount = amount.subtract(tax);

        target.setBalance(currency, target.getBalance(currency).add(taxedAmount));
        user.setBalance(currency, user.getBalance(currency).subtract(amount));

       // send message that will include original amount, taxed amount, tax rate - percentage amount and tax amount
        sender.sendMessage(
                miniMessage.deserialize("<yellow>Paid <gold>" + amount.toPlainString() + " <yellow>" + currency.getIdentifier() + " to " + target.getUsername() + " <yellow>(<gold>" + taxedAmount.toPlainString() + " <yellow>after tax)")
        );

        CompletableFuture.allOf(
                this.plugin.getUserManager().saveUser(user),
                this.plugin.getUserManager().saveUser(target)
        ).thenAccept(v -> removeFromMustWait(sender.getUniqueId(), target.getUniqueId()));
    }
}
