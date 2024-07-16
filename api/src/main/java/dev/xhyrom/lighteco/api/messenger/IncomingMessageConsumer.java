package dev.xhyrom.lighteco.api.messenger;

import dev.xhyrom.lighteco.api.messenger.message.Message;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IncomingMessageConsumer {
    void consumeIncomingMessage(@NonNull Message message);

    void consumeRawIncomingMessage(@NonNull String message);
}
