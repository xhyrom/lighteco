package dev.xhyrom.lighteco.api.model.currency;

import dev.xhyrom.lighteco.api.model.user.User;

import java.math.BigDecimal;
import java.text.NumberFormat;

public interface Currency {
    /**
     * Returns the identifier of the currency.
     *
     * @return the identifier
     */
    String getIdentifier();

    /**
     * Returns the type of the currency, either {@link Type#LOCAL} or {@link Type#GLOBAL}
     *
     * @see Type
     * @return The type of the currency
     */
    Type getType();

    /**
     * Format the given amount to a string
     *
     * @param amount The amount to format
     * @return The formatted amount
     */
    default String format(BigDecimal amount) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(fractionalDigits());

        return format.format(amount);
    }

    /**
     * Determine if this currency is payable
     *
     * @return `true` if this currency is payable, `false` otherwise
     */
    boolean isPayable();

    /**
     * Returns the number of fractional digits this currency has
     *
     * @return The number of fractional digits
     */
    default int fractionalDigits() {
        return 0;
    }

    /**
     * Returns the default balance for this currency
     *
     * @return The default balance
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
    enum Type {
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
