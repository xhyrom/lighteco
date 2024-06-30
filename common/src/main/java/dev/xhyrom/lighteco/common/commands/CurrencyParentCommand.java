package dev.xhyrom.lighteco.common.commands;

import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        return builder()
                .then(BalanceCommand.create(currency).build())
                .build();
    }
}
