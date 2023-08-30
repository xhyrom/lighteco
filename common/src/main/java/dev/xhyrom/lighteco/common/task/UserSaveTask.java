package dev.xhyrom.lighteco.common.task;

import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.Arrays;

public class UserSaveTask implements Runnable {
    private final LightEcoPlugin plugin;

    public UserSaveTask(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (this.plugin.getConfig().debug)
            this.plugin.getBootstrap().getLogger().info("Saving users in task");

        User[] users = this.plugin.getUserManager().values().stream()
                .filter(User::isDirty)
                .toArray(User[]::new);

        if (users.length == 0) {
            return;
        }

        try {
            this.plugin.getStorage().saveUsersSync(
                    Arrays.stream(users)
                            .map(User::getProxy)
                            .toArray(dev.xhyrom.lighteco.api.model.user.User[]::new)
            );

            for (User user : users) {
                user.setDirty(false);
            }
        } catch (RuntimeException e) {
            this.plugin.getBootstrap().getLogger().error("Failed to save users", e);
        }
    }
}
