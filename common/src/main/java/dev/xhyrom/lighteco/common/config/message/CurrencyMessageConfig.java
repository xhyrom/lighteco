package dev.xhyrom.lighteco.common.config.message;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Variable;

public class CurrencyMessageConfig extends OkaeriConfig {
    public String balance = "<currency> <dark_gray>| <gray>Your balance: <yellow><balance> </yellow></gray>";
    @Variable("balance-others")
    public String balanceOthers = "<currency> <dark_gray>| <gray>Balance of <yellow><target> <dark_gray>| <gray><yellow><balance> </yellow></gray>";

    public String set = "<currency> <dark_gray>| <gray>Set balance of <yellow><target> <dark_gray>| <gray><yellow><balance> </yellow></gray>";
    public String give = "<currency> <dark_gray>| <gray>Gave <yellow><target> <dark_gray>| <gray><yellow><balance> </yellow></gray>";
    public String take = "<currency> <dark_gray>| <gray>Took <yellow><target> <dark_gray>| <gray><yellow><balance> </yellow></gray>";

    public String pay = "<currency> <dark_gray>| <gray>Paid <gold><amount> <yellow>to <gold><target> <dark_gray>(<gold><taxed_amount> <yellow>after tax)</gray>";
}
