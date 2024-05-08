package dev.xhyrom.lighteco.api.messenger;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface MessengerProvider {
    @NonNull Messenger obtain(@NonNull IncomingMessageConsumer consumer);
}
