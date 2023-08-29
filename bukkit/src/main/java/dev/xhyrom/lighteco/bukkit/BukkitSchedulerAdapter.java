package dev.xhyrom.lighteco.bukkit;

import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerTask;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class BukkitSchedulerAdapter implements SchedulerAdapter {
    private final BukkitLightEcoBootstrap bootstrap;
    private final BukkitScheduler scheduler;

    public BukkitSchedulerAdapter(BukkitLightEcoBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.scheduler = bootstrap.getServer().getScheduler();
    }

    @Override
    public SchedulerTask asyncLater(Runnable runnable, long delay, TimeUnit unit) {
        return new Task(
                this.scheduler.runTaskLaterAsynchronously(
                        this.bootstrap.getLoader(),
                        runnable,
                        unit.toSeconds(delay) * 20
                )
        );
    }

    @Override
    public SchedulerTask asyncRepeating(Runnable runnable, long interval, TimeUnit unit) {
        return new Task(
                this.scheduler.runTaskTimerAsynchronously(
                        this.bootstrap.getLoader(),
                        runnable,
                        0,
                        unit.toSeconds(interval) * 20
                )
        );
    }

    public class Task implements SchedulerTask {
        private final BukkitTask task;
        public Task(BukkitTask task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            this.task.cancel();
        }
    }
}
