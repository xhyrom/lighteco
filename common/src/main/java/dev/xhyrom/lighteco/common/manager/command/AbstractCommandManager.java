package dev.xhyrom.lighteco.common.manager.command;

import dev.xhyrom.lighteco.api.exception.CannotBeGreaterThan;
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

public abstract class AbstractCommandManager implements CommandManager {
    public final LightEcoPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, CurrencyMessageConfig> config;
    private final ArrayList<UUID> mustWait = new ArrayList<>();

    protected AbstractCommandManager(LightEcoPlugin plugin) {
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
        CurrencyMessageConfig currencyMessageConfig = this.config.get(currency.getIdentifier());

        if (currencyMessageConfig == null) {
           return this.config.get("default");
        }

        return currencyMessageConfig;
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
        } catch (CannotBeGreaterThan e) {
            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getConfig(currency).cannotBeGreaterThan,
                            Placeholder.parsed("max", this.plugin.getConfig().maximumBalance.toPlainString())
                    )
            );

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

        removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
    }

    @Override
    public void onGive(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());
        amount = amount.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        try {
            target.deposit(currency, amount);
        } catch (CannotBeGreaterThan e) {
            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getConfig(currency).cannotBeGreaterThan,
                            Placeholder.parsed("max", this.plugin.getConfig().maximumBalance.toPlainString())
                    )
            );

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

        removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
    }

    @Override
    public void onTake(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        addToMustWait(sender.getUniqueId(), target.getUniqueId());
        amount = amount.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        try {
            target.withdraw(currency, amount);
        } catch (CannotBeGreaterThan e) {
            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getConfig(currency).cannotBeGreaterThan,
                            Placeholder.parsed("max", this.plugin.getConfig().maximumBalance.toPlainString())
                    )
            );

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

        removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
    }

    @Override
    public void onPay(CommandSender sender, Currency currency, User target, BigDecimal amount) {
        if (sender.getUniqueId() != null && (sender.getUniqueId() == target.getUniqueId())) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getConfig(currency).cannotPaySelf)
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
        tax = tax.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        // subtract tax from amount
        BigDecimal taxedAmount = amount.subtract(tax);

        try {
            target.deposit(currency, taxedAmount);
            user.withdraw(currency, amount);
        } catch (CannotBeGreaterThan e) {
            removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getConfig(currency).cannotBeGreaterThan,
                            Placeholder.parsed("max", this.plugin.getConfig().maximumBalance.toPlainString())
                    )
            );

            return;
        }

        String template = tax.compareTo(BigDecimal.ZERO) > 0
                ? this.getConfig(currency).payWithTax
                : this.getConfig(currency).pay;


        sender.sendMessage(
                miniMessage.deserialize(
                        template,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString()),
                        Placeholder.parsed("taxed_amount", taxedAmount.toPlainString()),
                        Placeholder.parsed("sender_balance", user.getBalance(currency).toPlainString()),
                        Placeholder.parsed("receiver_balance", target.getBalance(currency).toPlainString())
                )
        );

        removeFromMustWait(target.getUniqueId(), sender.getUniqueId());
    }
}
