package dev.xhyrom.lighteco.bukkit.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.bukkit.manager.BukkitCommandManager;
import dev.xhyrom.lighteco.bukkit.util.Util;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class PayCommand implements Command {
    private final BukkitCommandManager manager;
    private final Currency currency;
    private final String permissionBase;

    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand("pay")
                .withPermission(permissionBase + "pay")
                .withArguments(
                        new OfflinePlayerArgument("target"),
                        currency.getProxy().fractionalDigits() > 0
                                ? new DoubleArgument("amount", 1, Util.bigDecimalToDouble(this.manager.plugin.getConfig().maximumBalance))
                                : new IntegerArgument("amount", 1, this.manager.plugin.getConfig().maximumBalance.intValue())
                )
                .executesPlayer((sender, args) -> {
                    this.handlePay(sender, args, currency);
                });
    }

    private void handlePay(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.manager.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args.getRaw("amount")));

        if (!this.manager.canUse(sender, currency)) return;

        this.manager.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAccept(result -> {
                    String username = result.getUsername() == null ?
                            target.getName() != null
                                    ? target.getName()
                                    : args.getRaw("target")
                            : result.getUsername();
                    result.setUsername(username);

                    this.manager.onPay(sender, currency, result, amount);
                });
    }
}
