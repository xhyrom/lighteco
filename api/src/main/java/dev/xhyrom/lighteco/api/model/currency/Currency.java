package dev.xhyrom.lighteco.api.model.currency;

import dev.xhyrom.lighteco.api.exceptions.NotImplementedException;
import dev.xhyrom.lighteco.api.model.user.User;
import lombok.Getter;

import java.util.List;

public abstract class Currency<T> {
    @Getter
    private final Class<T> valueType;
    public Currency(Class<T> valueType) {
        this.valueType = valueType;
    }

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
     * Calculate the tax for the given amount
     * Used for payables
     *
     * @param amount The amount to calculate the tax for
     * @return The tax
     */
    public double calculateTax(T amount) {
        return 0;
    };

    // Implemented in common module
    public List<User> getTopUsers(int length) {
        throw new NotImplementedException();
    }

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
