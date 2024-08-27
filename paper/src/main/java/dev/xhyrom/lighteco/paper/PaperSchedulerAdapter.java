package dev.xhyrom.lighteco.paper;

import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerTask;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class PaperSchedulerAdapter implements SchedulerAdapter {
    private final Executor async;

    private final PaperLightEcoBootstrap bootstrap;
    private final BukkitScheduler scheduler;

    public PaperSchedulerAdapter(PaperLightEcoBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.scheduler = bootstrap.getLoader().getServer().getScheduler();

        this.async = runnable ->
                this.scheduler.runTaskAsynchronously(this.bootstrap.getLoader(), runnable);
    }

    public Executor async() {
        return this.async;
    }

    @Override
    public SchedulerTask asyncLater(Runnable runnable, long delay, TimeUnit unit) {
        return new Task(this.scheduler.runTaskLaterAsynchronously(
                this.bootstrap.getLoader(), runnable, unit.toSeconds(delay) * 20));
    }

    @Override
    public SchedulerTask asyncRepeating(Runnable runnable, long interval, TimeUnit unit) {
        return new Task(this.scheduler.runTaskTimerAsynchronously(
                this.bootstrap.getLoader(), runnable, 0, unit.toSeconds(interval) * 20));
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
