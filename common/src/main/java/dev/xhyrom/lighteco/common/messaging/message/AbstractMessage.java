package dev.xhyrom.lighteco.common.messaging.message;

import dev.xhyrom.lighteco.api.messenger.message.Message;
import dev.xhyrom.lighteco.api.messenger.message.OutgoingMessage;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public abstract class AbstractMessage implements Message, OutgoingMessage {
    private final UUID id;

    protected AbstractMessage(UUID id) {
        this.id = id;
    }

    @Override
    public @NonNull UUID getId() {
        return this.id;
    }
}
