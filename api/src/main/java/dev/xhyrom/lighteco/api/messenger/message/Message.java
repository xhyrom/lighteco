package dev.xhyrom.lighteco.api.messenger.message;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a message that can be received by a {@link dev.xhyrom.lighteco.api.messenger.Messenger}.
 */
public interface Message {
    @NonNull UUID getId();
}
