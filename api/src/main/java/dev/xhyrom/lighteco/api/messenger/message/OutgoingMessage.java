package dev.xhyrom.lighteco.api.messenger.message;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a message that can be sent via a {@link dev.xhyrom.lighteco.api.messenger.Messenger}.
 */
public interface OutgoingMessage extends Message {
    /**
     * Serializes message into a string.
     *
     * @return serialized message
     */
    @NonNull String serialize();
}
