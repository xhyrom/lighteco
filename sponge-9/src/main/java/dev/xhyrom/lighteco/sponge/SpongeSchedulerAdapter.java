package dev.xhyrom.lighteco.sponge;

import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SpongeSchedulerAdapter implements SchedulerAdapter {
    private final Executor async;

    protected SpongeSchedulerAdapter(SpongeLightEcoBootstrap bootstrap) {
        this.async = runnable -> Sponge.asyncScheduler().submit(Task.builder().execute(runnable).build());
    }

    private SchedulerTask submitAsyncTask(Runnable runnable, Consumer<Task.Builder> config) {
        Task.Builder builder = Task.builder();
        config.accept(builder);

        Task task = builder
                .execute(runnable)
                .build();

        ScheduledTask scheduledTask = Sponge.asyncScheduler().submit(task);
        return scheduledTask::cancel;
    }

    @Override
    public Executor async() {
        return this.async;
    }

    @Override
    public SchedulerTask asyncLater(Runnable runnable, long delay, TimeUnit unit) {
        return submitAsyncTask(runnable, builder -> builder.delay(delay, unit));
    }

    @Override
    public SchedulerTask asyncRepeating(Runnable runnable, long interval, TimeUnit unit) {
        return submitAsyncTask(runnable, builder -> builder.delay(interval, unit).interval(interval, unit));
    }
}
