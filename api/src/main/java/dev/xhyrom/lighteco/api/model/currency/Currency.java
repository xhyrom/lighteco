package dev.xhyrom.lighteco.api.model.currency;

public interface Currency<T> {
    String getIdentifier();

    /**
     * Get the type of the currency, either {@link Type#EXCLUSIVE} or {@link Type#GLOBAL}
     *
     * @see Type
     * @return The type of the currency
     */
    Type getType();

    boolean isPayable();

    /**
     * Calculate the tax for the given amount
     * Used for payables
     *
     * @param amount The amount to calculate the tax for
     * @return The tax
     */
    default double calculateTax(T amount) {
        return 0;
    };

    /**
     * Represents the type of currency
     */
    enum Type {
        /**
         * A currency that is only available on a single server
         */
        EXCLUSIVE,
        /**
         * A currency that is available on all servers (proxy)
         */
        GLOBAL;
    }
}
