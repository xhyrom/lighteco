package dev.xhyrom.lighteco.paper.hooks;

import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.paper.PaperLightEcoPlugin;

import lombok.RequiredArgsConstructor;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@RequiredArgsConstructor
public class PlaceholderAPIExpansion extends PlaceholderExpansion {
    private final PaperLightEcoPlugin plugin;

    @Override
    public @NonNull String getIdentifier() {
        return "lighteco";
    }

    @Override
    public @NonNull String getAuthor() {
        return this.plugin
                .getBootstrap()
                .getLoader()
                .getDescription()
                .getAuthors()
                .toString();
    }

    @Override
    public @NonNull String getVersion() {
        return this.plugin.getBootstrap().getLoader().getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NonNull String params) {
        String[] args = params.split("_");
        if (args.length < 2) return null;

        String currencyIdentifier = args[0];
        String type = args[1];

        Currency currency = this.plugin.getCurrencyManager().getIfLoaded(currencyIdentifier);
        if (currency == null) return null;

        User user = this.plugin.getUserManager().loadUser(player.getUniqueId()).join();

        switch (type.toLowerCase()) {
            case "balance" -> {
                return user.getBalance(currency).toPlainString();
            }
            case "balance_formatted" -> {
                return currency.format(user.getBalance(currency));
            }
        }

        return null;
    }
}
