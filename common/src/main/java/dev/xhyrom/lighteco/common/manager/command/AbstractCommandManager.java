package dev.xhyrom.lighteco.common.manager.command;

import dev.xhyrom.lighteco.common.config.message.CurrencyMessageConfig;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommandManager implements CommandManager {
    public final LightEcoPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, CurrencyMessageConfig> config;
    private final ArrayList<UUID> mustWait = new ArrayList<>();

    public AbstractCommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig().messages.currency;
    }

    @Override
    public boolean canUse(CommandSender sender, Currency currency) {
        // Console doesn't need to wait
        if (sender.getUniqueId() == null) return true;

        if (mustWait.contains(sender.getUniqueId())) {
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getConfig(currency).wait
                    )
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

    private CurrencyMessageConfig getConfig(Currency currency) {
        CurrencyMessageConfig config = this.config.get(currency.getIdentifier());

        if (config == null) {
           return this.config.get("default");
        }

        return config;
    }

    @Override
    public void onBalance(CommandSender sender, Currency currency) {
        User user = this.plugin.getUserManager().getIfLoaded(sender.getUniqueId());
        BigDecimal balance = user.getBalance(currency);

        sender.sendMessage(
               miniMessage.deserialize(
                       getConfig(currency).balance,
                       Placeholder.parsed("currency", currency.getIdentifier()),
                       Placeholder.parsed("balance", balance.toPlainString())
               )
        );
    }

    @Override
    public void onBalance(CommandSender sender, Currency currency, User target) {
        BigDecimal balance = target.getBalance(currency);

        sender.sendMessage(
                miniMessage.deserialize(
                        getConfig(currency).balanceOthers,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("balance", balance.toPlainString())
                )

        );
    }

    @Override
    public void onSet(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());
        amount = amount.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        try {
            target.setBalance(currency, amount);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).cannotSetNegative)
            );

            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            return;
        }

        sender.sendMessage(
                miniMessage.deserialize(
                        this.getConfig(currency).set,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString())
                )
        );

        this.plugin.getUserManager().saveUser(target)
                .thenAccept(v -> removeFromMustWait(target.getUniqueId(), sender.getUniqueId()));
    }

    @Override
    public void onGive(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());
        amount = amount.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        try {
            target.deposit(currency, amount);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).cannotGiveNegative)
            );

            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            return;
        }

        sender.sendMessage(
                miniMessage.deserialize(
                        this.getConfig(currency).give,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString()),
                        Placeholder.parsed("balance", target.getBalance(currency).toPlainString())
                )
        );

        this.plugin.getUserManager().saveUser(target)
                .thenAccept(v -> removeFromMustWait(target.getUniqueId(), sender.getUniqueId()));
    }

    @Override
    public void onTake(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());
        amount = amount.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        try {
            target.withdraw(currency, amount);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).cannotTakeNegative)
            );

            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            return;
        }

        sender.sendMessage(
                miniMessage.deserialize(
                        this.getConfig(currency).take,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString()),
                        Placeholder.parsed("balance", target.getBalance(currency).toPlainString())
                )
        );

        this.plugin.getUserManager().saveUser(target)
                .thenAccept(v -> removeFromMustWait(target.getUniqueId(), sender.getUniqueId()));
    }

    @Override
    public void onPay(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        if (sender.getUniqueId() != null && (sender.getUniqueId() == target.getUniqueId())) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).cannotPaySelf)
            );

            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).cannotPayNegative)
            );

            return;
        }

        amount = amount.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        User user = this.plugin.getUserManager().getIfLoaded(sender.getUniqueId());

        if (user.getBalance(currency).compareTo(amount) < 0) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).notEnoughMoney)
            );

            return;
        }

        addToMustWait(sender.getUniqueId(), target.getUniqueId());

        // calculate tax using Currency#calculateTax
        BigDecimal tax = currency.getProxy().calculateTax(user.getProxy(), amount);

        // subtract tax from amount
        BigDecimal taxedAmount = amount.subtract(tax);

        target.deposit(currency, taxedAmount);
        user.withdraw(currency, amount);

        sender.sendMessage(
                miniMessage.deserialize(
                        this.getConfig(currency).pay,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString()),
                        Placeholder.parsed("taxed_amount", taxedAmount.toPlainString()),
                        Placeholder.parsed("sender_balance", user.getBalance(currency).toPlainString()),
                        Placeholder.parsed("receiver_balance", target.getBalance(currency).toPlainString())
                )
        );

        CompletableFuture.allOf(
                this.plugin.getUserManager().saveUser(user),
                this.plugin.getUserManager().saveUser(target)
        ).thenAccept(v -> removeFromMustWait(sender.getUniqueId(), target.getUniqueId()));
    }
}
