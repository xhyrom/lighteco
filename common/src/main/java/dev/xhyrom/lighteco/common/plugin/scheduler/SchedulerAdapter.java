package dev.xhyrom.lighteco.common.plugin.scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public interface SchedulerAdapter {
    Executor async();

    SchedulerTask asyncLater(Runnable runnable, long delay, TimeUnit unit);

    SchedulerTask asyncRepeating(Runnable runnable, long interval, TimeUnit unit);
}
