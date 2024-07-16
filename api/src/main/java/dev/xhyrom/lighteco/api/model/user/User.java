package dev.xhyrom.lighteco.api.model.user;

import dev.xhyrom.lighteco.api.model.currency.Currency;

import net.kyori.adventure.text.Component;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public interface User {
    /**
     * Get the unique id of this user.
     *
     * @return the unique id
     */
    @NonNull UUID getUniqueId();

    /**
     * Get the username of this user.
     *
     * @return the username
     */
    @Nullable String getUsername();

    /**
     * Get the balance of this user for the specified currency.
     *
     * @param currency the currency
     * @return the balance
     */
    BigDecimal getBalance(@NonNull Currency currency);

    /**
     * Set the balance of this user for the specified currency.
     * <p>
     *     Save the user after setting the balance using {@link dev.xhyrom.lighteco.api.manager.UserManager#saveUser(User)}. <br/>
     *     If you're doing multiple changes, use {@link dev.xhyrom.lighteco.api.manager.UserManager#saveUsers(User...)}
     * </p>
     *
     * @param currency the currency
     * @param balance the balance
     */
    void setBalance(@NonNull Currency currency, @NonNull BigDecimal balance);

    /**
     * Add the specified amount to the balance of this user for the specified currency.
     *
     * @param currency the currency
     * @param amount the amount
     * @throws IllegalArgumentException if the amount is negative
     */
    void deposit(@NonNull Currency currency, @NonNull BigDecimal amount)
            throws IllegalArgumentException;

    /**
     * Subtract the specified amount from the balance of this user for the specified currency.
     *
     * @param currency the currency
     * @param amount the amount
     * @throws IllegalArgumentException if the amount is negative
     */
    void withdraw(@NonNull Currency currency, @NonNull BigDecimal amount)
            throws IllegalArgumentException;

    /**
     * Send a message to this user.
     * Message will be silently dropped if the user is offline.
     *
     * @param message the message
     */
    void sendMessage(Component message);
}
