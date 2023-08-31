package dev.xhyrom.lighteco.common.manager.user;

import dev.xhyrom.lighteco.common.cache.ExpiringSet;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserHousekeeper implements Runnable {
    private final LightEcoPlugin plugin;
    private final UserManager userManager;

    private final ExpiringSet<UUID> recentlyUsed;

    public UserHousekeeper(LightEcoPlugin plugin, UserManager userManager, TimeoutSettings timeoutSettings) {
        this.plugin = plugin;
        this.userManager = userManager;
        this.recentlyUsed = new ExpiringSet<>(timeoutSettings.duration, timeoutSettings.unit);
    }

    public void registerUsage(UUID uuid) {
        this.recentlyUsed.add(uuid);
    }

    @Override
    public void run() {
        for (UUID entry : this.userManager.keys()) {
            cleanup(entry);
        }
    }

    public void cleanup(UUID uuid) {
        if (this.recentlyUsed.contains(uuid) || this.plugin.getBootstrap().isPlayerOnline(uuid)) {
            return;
        }

        User user = this.userManager.getIfLoaded(uuid);
        if (user == null) {
            return;
        }

        // If the user is dirty (has unsaved changes), don't unload
        if (user.isDirty())
            return;

        if (this.plugin.getConfig().debug) {
            this.plugin.getBootstrap().getLogger().info("Unloading data for " + uuid);
        }

        this.userManager.unload(uuid);
    }

    public static TimeoutSettings timeoutSettings(long duration, TimeUnit unit) {
        return new TimeoutSettings(duration, unit);
    }

    public static final class TimeoutSettings {
        private final long duration;
        private final TimeUnit unit;

        TimeoutSettings(long duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
        }
    }
}