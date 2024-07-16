package dev.xhyrom.lighteco.common.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.command.suggestion.type.OfflineUserSuggestionProvider;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static dev.xhyrom.lighteco.common.command.CommandHelper.*;

public class BalanceCommand extends Command {
    private final Currency currency;

    public static BalanceCommand create(@NonNull Currency currency) {
        return new BalanceCommand(currency);
    }

    public BalanceCommand(@NonNull Currency currency) {
        super("balance", "Check your balance");

        this.currency = currency;
    }

    @Override
    public CommandNode<CommandSource> build() {
        return builder()
                .then(
                        RequiredArgumentBuilder.<CommandSource, String>argument("target", StringArgumentType.word())
                                .suggests(OfflineUserSuggestionProvider.create())
                                .requires((source) -> source.sender().eligible("lighteco.currency."+currency.getIdentifier()+".command.balance.others"))
                                .executes(context -> {
                                    LightEcoPlugin plugin = context.getSource().plugin();
                                    CommandSender sender = context.getSource().sender();

                                    final User target = getUser(context);
                                    if (target == null)
                                        return SINGLE_SUCCESS;

                                    BigDecimal balance = target.getBalance(currency);

                                    sender.sendMessage(
                                            MiniMessage.miniMessage().deserialize(
                                                    getCurrencyMessageConfig(plugin, currency).balanceOthers,
                                                    Placeholder.parsed("currency", currency.getIdentifier()),
                                                    Placeholder.parsed("target", target.getUsername()),
                                                    Placeholder.parsed("balance", balance.toPlainString())
                                            )
                                    );

                                    return SINGLE_SUCCESS;
                                }))
                .requires((source) -> source.sender().eligible("lighteco.currency."+currency.getIdentifier()+".command.balance"))
                .executes(context -> {
                    LightEcoPlugin plugin = context.getSource().plugin();
                    CommandSender sender = context.getSource().sender();

                    User user = plugin.getUserManager().getIfLoaded(sender.getUniqueId());
                    BigDecimal balance = user.getBalance(currency);

                    sender.sendMessage(
                            MiniMessage.miniMessage().deserialize(
                                    getCurrencyMessageConfig(plugin, currency).balance,
                                    Placeholder.parsed("currency", currency.getIdentifier()),
                                    Placeholder.parsed("balance", balance.toPlainString())
                            )
                    );

                    return SINGLE_SUCCESS;
                })
                .build();
    }
}
