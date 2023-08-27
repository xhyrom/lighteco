package dev.xhyrom.lighteco.bukkit.hooks;

import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
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
        return "lighteco";
    }
}
