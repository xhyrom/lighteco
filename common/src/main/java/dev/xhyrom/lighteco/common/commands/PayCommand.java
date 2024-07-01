package dev.xhyrom.lighteco.common.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.api.exception.CannotBeGreaterThan;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.command.argument.type.OfflineUserArgument;
import dev.xhyrom.lighteco.common.command.suggestion.type.OfflineUserSuggestionProvider;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PayCommand extends Command {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Currency currency;

    public static PayCommand create(@NonNull Currency currency) {
        return new PayCommand(currency);
    }

    public PayCommand(@NonNull Currency currency) {
        super("pay", "Pay a player");

        this.currency = currency;
    }

    private void execute(CommandContext<CommandSource> context) {
        final LightEcoPlugin plugin = context.getSource().plugin();
        final CommandSender sender = context.getSource().sender();

        final User target = OfflineUserArgument.getOfflineUser(context, "target");
        if (target == null || target.getUsername() == null) {
            return;
        }

        BigDecimal amount = BigDecimal.valueOf(currency.fractionalDigits() > 0
                ? context.getArgument("amount", Double.class)
                : context.getArgument("amount", Integer.class));

        amount = amount.setScale(currency.fractionalDigits(), RoundingMode.DOWN);

        final User user = plugin.getUserManager().getIfLoaded(sender.getUniqueId());
        if (user == null) {
            return;
        }

        if (user.getBalance(this.currency).compareTo(amount) < 0) {
            sender.sendMessage(
                    miniMessage.deserialize(this.getCurrencyMessageConfig(plugin, this.currency).notEnoughMoney)
            );

            return;
        }

        // calculate tax using Currency#calculateTax
        BigDecimal tax = currency.getProxy().calculateTax(user.getProxy(), amount);
        tax = tax.setScale(currency.getProxy().fractionalDigits(), RoundingMode.DOWN);

        // subtract tax from amount
        BigDecimal taxedAmount = amount.subtract(tax);

        try {
            target.deposit(currency, taxedAmount);
            user.withdraw(currency, amount);
        } catch (CannotBeGreaterThan e) {
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getCurrencyMessageConfig(plugin, this.currency).cannotBeGreaterThan,
                            Placeholder.parsed("max", plugin.getConfig().maximumBalance.toPlainString())
                    )
            );

            return;
        }

        String template = tax.compareTo(BigDecimal.ZERO) > 0
                ? this.getCurrencyMessageConfig(plugin, this.currency).payWithTax
                : this.getCurrencyMessageConfig(plugin, this.currency).pay;

        String templateReceived = tax.compareTo(BigDecimal.ZERO) > 0
                ? this.getCurrencyMessageConfig(plugin, this.currency).payReceivedWithTax
                : this.getCurrencyMessageConfig(plugin, this.currency).payReceived;

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

        target.sendMessage(
                miniMessage.deserialize(
                        templateReceived,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("sender", user.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString()),
                        Placeholder.parsed("taxed_amount", taxedAmount.toPlainString()),
                        Placeholder.parsed("sender_balance", user.getBalance(currency).toPlainString()),
                        Placeholder.parsed("receiver_balance", target.getBalance(currency).toPlainString())
                )
        );
    }

    @Override
    public CommandNode<CommandSource> build() {
        if (currency.fractionalDigits() > 0) {
            return builder()
                    .then(RequiredArgumentBuilder.<CommandSource, String>argument("target", StringArgumentType.word())
                            .suggests(OfflineUserSuggestionProvider.create())
                            .then(RequiredArgumentBuilder.<CommandSource, Double>argument("amount", DoubleArgumentType.doubleArg(1))
                                    .executes(c -> {
                                        execute(c);
                                        return SINGLE_SUCCESS;
                                    })))
                            .build();
        }

        return builder()
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("target", StringArgumentType.word())
                        .suggests(OfflineUserSuggestionProvider.create())
                        .then(RequiredArgumentBuilder.<CommandSource, Integer>argument("amount", IntegerArgumentType.integer(1))
                                .executes(c -> {
                                    execute(c);
                                    return SINGLE_SUCCESS;
                                })))
                .build();
    }
}
