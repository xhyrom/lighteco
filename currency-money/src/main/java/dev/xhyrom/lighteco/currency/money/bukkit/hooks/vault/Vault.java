package dev.xhyrom.lighteco.currency.money.bukkit.hooks.vault;

import dev.xhyrom.lighteco.api.LightEco;
import dev.xhyrom.lighteco.api.LightEcoProvider;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Vault extends AbstractEconomy {
    private final LightEco provider;
    private final Currency currency;

    public Vault() {
        this.provider = LightEcoProvider.get();
        this.currency = this.provider.getCurrencyManager().getCurrency("money");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "LightEco";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return currency.fractionalDigits();
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(playerName, null);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        UUID uniqueId = Bukkit.getPlayerUniqueId(playerName);
        assert uniqueId != null;

        return provider.getUserManager().isLoaded(uniqueId);
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(playerName, null);
    }

    @Override
    public double getBalance(String playerName, String world) {
        UUID uniqueId = Bukkit.getPlayerUniqueId(playerName);
        User user = provider.getUserManager().loadUser(uniqueId).join();

        return bigDecimalToDouble(user.getBalance(currency));
    }

    @Override
    public boolean has(String playerName, double amount) {
       return has(playerName, null, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        UUID uniqueId = Bukkit.getPlayerUniqueId(playerName);
        User user = provider.getUserManager().loadUser(uniqueId).join();

        return user.getBalance(currency).compareTo(BigDecimal.valueOf(amount)) >= 0;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(playerName, null, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        UUID uniqueId = Bukkit.getPlayerUniqueId(playerName);
        User user = provider.getUserManager().loadUser(uniqueId).join();

        BigDecimal balance = user.getBalance(currency);
        BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(amount));

        user.setBalance(currency, newBalance);

        provider.getUserManager().saveUser(user);

        return new EconomyResponse(amount, bigDecimalToDouble(newBalance), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(playerName, null, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        UUID uniqueId = Bukkit.getPlayerUniqueId(playerName);
        User user = provider.getUserManager().loadUser(uniqueId).join();

        BigDecimal balance = user.getBalance(currency);
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));

        user.setBalance(currency, newBalance);

        provider.getUserManager().saveUser(user);

        return new EconomyResponse(amount, bigDecimalToDouble(newBalance), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(playerName, null);
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        UUID uniqueId = Bukkit.getPlayerUniqueId(playerName);
        provider.getUserManager().loadUser(uniqueId).join();

        return true;
    }

    private double bigDecimalToDouble(final BigDecimal value) {
        double amount = value.doubleValue();

        if (new BigDecimal(amount).compareTo(value) > 0) {
            amount = Math.nextAfter(amount, Double.NEGATIVE_INFINITY);
        }

        return amount;
    }
}
