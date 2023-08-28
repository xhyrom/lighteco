package dev.xhyrom.lighteco.bukkit.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.bukkit.manager.BukkitCommandManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class TakeCommand implements Command {
    private final BukkitCommandManager manager;
    private final Currency currency;
    private final String permissionBase;

    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand("take")
                .withPermission(permissionBase + "take")
                .withArguments(
                        new OfflinePlayerArgument("target"),
                        currency.getProxy().fractionalDigits() > 0
                                ? new DoubleArgument("amount", 1)
                                : new IntegerArgument("amount", 1)
                )
                .executes((sender, args) -> {
                    this.handleTake(sender, args, currency);
                });
    }


    private void handleTake(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.manager.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args.getRaw("amount")));

        if (!this.manager.canUse(sender, currency)) return;

        this.manager.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAcceptAsync(result -> {
                    String name = target.getName() != null ? target.getName() : args.getRaw("target");
                    result.setUsername(name);

                    this.manager.onTake(sender, currency, result, amount);
                });
    }
}
