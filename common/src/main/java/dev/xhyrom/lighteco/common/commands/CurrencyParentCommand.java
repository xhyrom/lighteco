package dev.xhyrom.lighteco.common.commands;

import dev.xhyrom.lighteco.common.command.abstraction.ParentCommand;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CurrencyParentCommand extends ParentCommand {
    public CurrencyParentCommand(@NonNull Currency currency) {
        super(
                currency.getIdentifier(),
                currency.getIdentifier(),
                BalanceCommand.create(currency),
                BalanceOtherCommand.create(currency)
        );
    }
}
