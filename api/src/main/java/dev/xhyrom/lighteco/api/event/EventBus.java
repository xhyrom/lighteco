package dev.xhyrom.lighteco.api.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

public interface EventBus {
    <T extends LightEcoEvent> @NonNull EventSubscription<T> subscribe(@NonNull Class<T> eventClass, @NonNull Consumer<? super T> subscriber);

    <T extends LightEcoEvent> @NonNull EventSubscription<T> subscribe(Object plugin, @NonNull Class<T> eventClass, @NonNull Consumer<? super T> subscriber);
}
