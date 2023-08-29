package dev.xhyrom.lighteco.api.model.currency;

import dev.xhyrom.lighteco.api.model.user.User;

import java.math.BigDecimal;

public interface Currency {
    String getIdentifier();
    /**
     * Get the type of the currency, either {@link Type#LOCAL} or {@link Type#GLOBAL}
     *
     * @see Type
     * @return The type of the currency
     */
    Type getType();

    boolean isPayable();

    /**
     * Get the number of fractional digits this currency has
     *
     * @return The number of fractional digits
     */
    default int fractionalDigits() {
        return 0;
    };

    /**
     * Get the users that have a balance in this currency
     *
     * @return The users
     */
    BigDecimal getDefaultBalance();

    /**
     * Calculate the tax for the given amount
     * Used for payables
     *
     * @param amount The amount to calculate the tax for
     * @return Amount that should be taxed
     */
    default BigDecimal calculateTax(User user, BigDecimal amount) {
        return BigDecimal.ZERO;
    };

    /**
     * Represents the type of currency
     */
    public enum Type {
        /**
         * A currency that is only available on a single server
         */
        LOCAL,
        /**
         * A currency that is available on all servers (proxy)
         */
        GLOBAL;
    }
}
