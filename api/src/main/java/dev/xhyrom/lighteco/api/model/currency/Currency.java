package dev.xhyrom.lighteco.api.model.currency;

import java.math.BigDecimal;

public abstract class Currency {
    public abstract String getIdentifier();
    /**
     * Get the type of the currency, either {@link Type#LOCAL} or {@link Type#GLOBAL}
     *
     * @see Type
     * @return The type of the currency
     */
    public abstract Type getType();

    public abstract boolean isPayable();

    /**
     * Get the number of decimal places this currency has
     * If zero, the currency is considered to be a whole number (integer)
     *
     * @return The number of decimal places
     */
    public int getDecimalPlaces() {
        return 0;
    };

    /**
     * Get the users that have a balance in this currency
     *
     * @return The users
     */
    public abstract BigDecimal getDefaultBalance();

    /**
     * Calculate the tax for the given amount
     * Used for payables
     *
     * @param amount The amount to calculate the tax for
     * @return The tax
     */
    public BigDecimal calculateTax(BigDecimal amount) {
        return getDefaultBalance();
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
