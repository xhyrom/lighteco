package dev.xhyrom.lighteco.bukkit.hooks;

import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PlaceholderAPIExpansion extends PlaceholderExpansion {
    private final BukkitLightEcoPlugin plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "lighteco";
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getBootstrap().getLoader().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getBootstrap().getLoader().getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] args = params.split("_");
        if (args.length < 2) return null;

        String currencyIdentifier = args[0];
        String type = args[1];

        Currency currency = this.plugin.getCurrencyManager().getIfLoaded(currencyIdentifier);
        if (currency == null) return null;

        if (type.equalsIgnoreCase("balance")) {
            User user = this.plugin.getUserManager().loadUser(player.getUniqueId()).join();

            return user.getBalance(currency).toPlainString();
        }

        return "lighteco";
    }
}
