package dev.xhyrom.lighteco.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static dev.xhyrom.lighteco.common.command.CommandHelper.getCurrencyMessageConfig;

public class CurrencyParentCommand extends Command {
    private final Currency currency;

    public CurrencyParentCommand(@NonNull Currency currency) {
        super(
                currency.getIdentifier(),
                currency.getIdentifier()
        );

        this.currency = currency;
    }

    @Override
    public CommandNode<CommandSource> build() {
        LiteralArgumentBuilder<CommandSource> builder = builder()
                .then(BalanceCommand.create(currency).build())
                .then(SetCommand.create(currency).build())
                .then(GiveCommand.create(currency).build())
                .then(TakeCommand.create(currency).build())
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
                });

        if (currency.isPayable())
            builder = builder.then(PayCommand.create(currency).build());

        return builder.build();
    }
}
