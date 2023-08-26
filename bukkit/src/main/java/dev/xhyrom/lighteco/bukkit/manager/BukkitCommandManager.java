package dev.xhyrom.lighteco.bukkit.manager;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.common.manager.command.AbstractCommandManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.util.List;

public class BukkitCommandManager extends AbstractCommandManager {
    private final BukkitAudiences audienceFactory;

    public BukkitCommandManager(LightEcoPlugin plugin) {
        super(plugin);

        this.audienceFactory = BukkitAudiences.create((JavaPlugin) this.plugin.getBootstrap().getLoader());
    }

    private List<CommandAPICommand> getBalanceCommands(String name, String permissionBase, Currency currency) {
        return List.of(
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
        );
    }

    @Override
    public void registerCurrencyCommand(@NonNull Currency currency) {
        String permissionBase = "lighteco.currency." + currency.getIdentifier() + ".command.";

        // Balance
        getBalanceCommands(currency.getIdentifier(), permissionBase, currency)
                .forEach(CommandAPICommand::register);

        List<CommandAPICommand> balanceCommands = getBalanceCommands("balance", permissionBase, currency);

        new CommandAPICommand(currency.getIdentifier())
                .withSubcommand(new CommandAPICommand("set")
                        .withPermission(permissionBase + "set")
                        .withArguments(
                                new OfflinePlayerArgument("target"),
                                currency.getProxy().getDecimalPlaces() > 0
                                        ? new DoubleArgument("amount", 0)
                                        : new IntegerArgument("amount", 0)
                        )
                        .executes((sender, args) -> {
                            this.handleSet(sender, args, currency);
                        })
                )
                .withSubcommand(new CommandAPICommand("give")
                        .withPermission(permissionBase + "give")
                        .withArguments(
                                new OfflinePlayerArgument("target"),
                                currency.getProxy().getDecimalPlaces() > 0
                                        ? new DoubleArgument("amount", 1)
                                        : new IntegerArgument("amount", 1)
                        )
                        .executes((sender, args) -> {
                            this.handleGive(sender, args, currency);
                        })
                )
                .withSubcommand(new CommandAPICommand("take")
                        .withPermission(permissionBase + "take")
                        .withArguments(
                                new OfflinePlayerArgument("target"),
                                currency.getProxy().getDecimalPlaces() > 0
                                        ? new DoubleArgument("amount", 1)
                                        : new IntegerArgument("amount", 1)
                        )
                        .executes((sender, args) -> {
                            this.handleTake(sender, args, currency);
                        })
                )
                .withSubcommand(new CommandAPICommand("pay")
                        .withPermission(permissionBase + "pay")
                        .withArguments(
                                new OfflinePlayerArgument("target"),
                                currency.getProxy().getDecimalPlaces() > 0
                                        ? new DoubleArgument("amount", 1)
                                        : new IntegerArgument("amount", 1)
                        )
                        .executesPlayer((sender, args) -> {
                            this.handlePay(sender, args, currency);
                        })
                )
                .withSubcommand(balanceCommands.get(0))
                .withSubcommands(balanceCommands.get(1))
                .register();
    }

    private void handleBalance(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");

        if (target == null) {
            this.onBalance(sender, currency);

            return;
        }

        this.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAcceptAsync(result -> {
                    String name = target.getName() != null ? target.getName() : args.getRaw("target");
                    result.setUsername(name);

                    this.onBalance(sender, currency, result);
                });
    }

    private void handleSet(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args.getRaw("amount")));

        if (!this.canUse(sender)) return;

        this.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAcceptAsync(result -> {
                    String name = target.getName() != null ? target.getName() : args.getRaw("target");
                    result.setUsername(name);

                    this.onSet(sender, currency, result, amount);
                });
    }

    private void handleGive(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args.getRaw("amount")));

        if (!this.canUse(sender)) return;

        this.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAcceptAsync(result -> {
                    String name = target.getName() != null ? target.getName() : args.getRaw("target");
                    result.setUsername(name);

                    this.onGive(sender, currency, result, amount);
                });
    }

    private void handleTake(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args.getRaw("amount")));

        if (!this.canUse(sender)) return;

        this.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAcceptAsync(result -> {
                    String name = target.getName() != null ? target.getName() : args.getRaw("target");
                    result.setUsername(name);

                    this.onTake(sender, currency, result, amount);
                });
    }

    private void handlePay(CommandSender originalSender, CommandArguments args, Currency currency) {
        BukkitCommandSender sender = new BukkitCommandSender(originalSender, this.audienceFactory);
        OfflinePlayer target = (OfflinePlayer) args.get("target");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args.getRaw("amount")));

        if (!this.canUse(sender)) return;

        this.plugin.getUserManager().loadUser(target.getUniqueId())
                .thenAcceptAsync(result -> {
                    String name = target.getName() != null ? target.getName() : args.getRaw("target");
                    result.setUsername(name);

                    this.onPay(sender, currency, result, amount);
                });
    }
}
