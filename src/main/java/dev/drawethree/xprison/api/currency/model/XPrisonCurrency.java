package dev.drawethree.xprison.api.currency.model;

/**
 * Represents the metadata and formatting configuration of a currency in the XPrison API.
 * This includes display name, format rules, symbol settings, and optional icon.
 * <p>
 * Currency balance operations are handled by a separate CurrencyService.
 */
public interface XPrisonCurrency {

    /**
     * Gets the internal ID or key of the currency (e.g., "money", "tokens").
     *
     * @return The unique name of this currency.
     */
    String getName();

    /**
     * Gets the maximum amount of a currency player can have
     *
     * @return maximum amount (cap) of a currency for player
     */
    double getMaxAmount();

    /**
     * Gets the display name of the currency (e.g., "Money", "Tokens").
     *
     * @return The friendly display name of the currency.
     */
    String getDisplayName();

    /**
     * Gets the prefix of the currency (e.g., "$").
     *
     * @return The friendly display name of the currency.
     */
    String getPrefix();

    /**
     * Gets the suffix of the currency.
     *
     * @return The friendly display name of the currency.
     */
    String getSuffix();

    /**
     * Formats a raw double value according to the currency's configuration.
     *
     * @param amount The amount to format.
     * @return The formatted currency string (e.g., "$1.2k", "$3,000").
     */
    String format(double amount);

    /**
     * Retrieves the custom {@link XPrisonCurrencyHandler} responsible for managing balance operations
     * for this currency. If this method returns {@code null}, the currency is assumed to be managed
     * internally by XPrison's default currency system.
     * <p>
     * Developers can override this method to provide custom handling logic, such as integration with
     * external economy plugins or databases.
     *
     * @return The {@link XPrisonCurrencyHandler} for this currency, or {@code null} if handled internally.
     */
    default XPrisonCurrencyHandler getHandler() {
        return null;
    }
}
