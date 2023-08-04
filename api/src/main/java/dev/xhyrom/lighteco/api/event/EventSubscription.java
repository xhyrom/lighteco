package dev.xhyrom.lighteco.api.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

public interface EventSubscription<T extends LightEcoEvent> extends AutoCloseable {
    @NonNull Class<T> getEventClass();

    boolean isActive();

    @NonNull Consumer<? super T> getHandler();
}
