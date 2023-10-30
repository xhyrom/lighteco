package dev.xhyrom.lighteco.common.config.message;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Variable;

public class CurrencyMessageConfig extends OkaeriConfig {
    public String balance = "<currency> <dark_gray>| <gray>Your balance: <yellow><balance> </yellow></gray>";
    @Variable("balance-others")
    public String balanceOthers = "<currency> <dark_gray>| <gray>Balance of <yellow><target> <dark_gray>| <gray><yellow><balance> </yellow></gray>";

    public String set = "<currency> <dark_gray>| <gray>Set balance of <gold><target> <yellow>to <gold><amount>";
    public String give = "<currency> <dark_gray>| <gray>Gave <gold><target> <gold><amount> <dark_gray>| <gold><balance>";
    public String take = "<currency> <dark_gray>| <gray>Took <gold><amount> <yellow>from <gold><target>";

    public String pay = "<currency> <dark_gray>| <gray>Paid <gold><amount> <yellow>to <gold><target>";
    public String payWithTax = "<currency> <dark_gray>| <gray>Paid <gold><amount> <yellow>to <gold><target> <dark_gray>(<gold><taxed_amount> <yellow>after tax<dark_gray>)";
    public String payReceived = "<currency> <dark_gray>| <gray>Received <gold><amount> <yellow>from <gold><sender>";
    public String payReceivedWithTax = "<currency> <dark_gray>| <gray>Received <gold><amount> <yellow>from <gold><sender> <dark_gray>(<gold><taxed_amount> <yellow>after tax<dark_gray>)";

    public String wait = "<red>Please wait a moment before using this command again.";
    public String notEnoughMoney = "<red>You don't have enough money!";
    public String cannotPaySelf = "<red>You cannot pay yourself!";
    public String cannotBeGreaterThan = "<red>Amount cannot be greater than <gold><max>";
}
