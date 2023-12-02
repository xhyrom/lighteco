package dev.xhyrom.lighteco.api.messenger.message.type;

import dev.xhyrom.lighteco.api.messenger.message.Message;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a message that is sent when a user updates their profile.
 */
public interface UserUpdateMessage extends Message {
    /**
     * Gets the unique id of the user that updated their profile.
     *
     * @return the user's unique id
     */
    @NonNull UUID getUserUniqueId();

    @NonNull String getCurrencyIdentifier();

    @NonNull BigDecimal getNewBalance();
}
