package dev.xhyrom.lighteco.common.task;

import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.concurrent.ExecutionException;

public class UserSaveTask implements Runnable {
    private final LightEcoPlugin plugin;

    public UserSaveTask(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        User[] users = this.plugin.getUserManager().values().stream()
                .filter(User::isDirty)
                .toArray(User[]::new);

        if (users.length == 0) {
            return;
        }

        try {
            this.plugin.getUserManager().saveUsers(users).get();

            for (User user : users) {
                user.setDirty(false);
            }
        } catch (InterruptedException | RuntimeException | ExecutionException e) {
            this.plugin.getBootstrap().getLogger().error("Failed to save users", e);
        }
    }
}
