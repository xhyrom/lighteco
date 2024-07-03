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

public class SetCommand extends Command {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Currency currency;

    public static SetCommand create(@NonNull Currency currency) {
        return new SetCommand(currency);
    }

    public SetCommand(@NonNull Currency currency) {
        super("set", "Set a player's balance");

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

        try {
            target.setBalance(currency, amount);
        } catch (CannotBeGreaterThan e) {
            sender.sendMessage(
                    miniMessage.deserialize(
                            this.getCurrencyMessageConfig(plugin, this.currency).cannotBeGreaterThan,
                            Placeholder.parsed("max", plugin.getConfig().maximumBalance.toPlainString())
                    )
            );

            return;
        }

        sender.sendMessage(
                miniMessage.deserialize(
                        this.getCurrencyMessageConfig(plugin, this.currency).set,
                        Placeholder.parsed("currency", currency.getIdentifier()),
                        Placeholder.parsed("target", target.getUsername()),
                        Placeholder.parsed("amount", amount.toPlainString())
                )
        );
    }

    @Override
    public CommandNode<CommandSource> build() {
        if (currency.fractionalDigits() > 0) {
            return builder()
                    .requires((source) -> source.sender().eligible("lighteco.currency." + currency.getIdentifier() + ".command.set"))
                    .then(RequiredArgumentBuilder.<CommandSource, String>argument("target", StringArgumentType.word())
                            .suggests(OfflineUserSuggestionProvider.create())
                            .then(RequiredArgumentBuilder.<CommandSource, Double>argument("amount", DoubleArgumentType.doubleArg(1))
                                    .requires((source) -> source.sender().eligible("lighteco.currency." + currency.getIdentifier() + ".command.set"))
                                    .executes(c -> {
                                        execute(c);
                                        return SINGLE_SUCCESS;
                                    })))
                            .build();
        }

        return builder()
                .requires((source) -> source.sender().eligible("lighteco.currency." + currency.getIdentifier() + ".command.set"))
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("target", StringArgumentType.word())
                        .suggests(OfflineUserSuggestionProvider.create())
                        .then(RequiredArgumentBuilder.<CommandSource, Integer>argument("amount", IntegerArgumentType.integer(1))
                                .requires((source) -> source.sender().eligible("lighteco.currency." + currency.getIdentifier() + ".command.set"))
                                .executes(c -> {
                                    execute(c);
                                    return SINGLE_SUCCESS;
                                })))
                .build();
    }
}
