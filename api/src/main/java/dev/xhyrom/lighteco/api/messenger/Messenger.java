package dev.xhyrom.lighteco.api.messenger;

import dev.xhyrom.lighteco.api.messenger.message.OutgoingMessage;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface Messenger extends AutoCloseable {
    void sendOutgoingMessage(@NonNull OutgoingMessage message, boolean global);

    @Override
    default void close() {}
}
