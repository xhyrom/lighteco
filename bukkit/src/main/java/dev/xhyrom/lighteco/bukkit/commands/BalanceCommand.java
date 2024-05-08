package dev.xhyrom.lighteco.bukkit.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.bukkit.manager.BukkitCommandManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class BalanceCommand implements Command {
    private final BukkitCommandManager manager;
    private final String name;
    private final Currency currency;
    private final String permissionBase;

    @Override
    public CommandAPICommand[] multipleBuild() {
        return new CommandAPICommand[]{
                new CommandAPICommand(name)
                        .withPermission(permissionBase + "balance.others")
                        .withArguments(new OfflinePlayerArgument("target"))
                        .executes((sender, args) -> {
                            this.handleBalance(sender, args, currency);
                        }),
                new CommandAPICommand(name)
                        .withPermission(permissionBase + "balance")
                        .executesPlayer((sender, args) -> {
                            this.handleBalance(sender, args, currency);
                        })
        };
    }

    public void handleBalance(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.manager.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");

        if (target == null) {
            this.manager.onBalance(sender, currency);

            return;
        }

        this.manager.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAccept(result -> {
                    String username = result.getUsername() == null ?
                            target.getName() != null
                                    ? target.getName()
                                    : args.getRaw("target")
                            : result.getUsername();
                    result.setUsername(username);

                    this.manager.onBalance(sender, currency, result);
                });
    }
}
