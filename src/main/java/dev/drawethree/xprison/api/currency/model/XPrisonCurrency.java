package dev.drawethree.xprison.api.currency.model;

/**
 * Represents the metadata and formatting configuration of a currency in the XPrison API.
 * This includes display name, format rules, symbol settings, and optional withdraw item.
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
     * Returns the initial balance assigned to a new player for this currency.
     */
    double getStartingAmount();

    /**
     * Returns the {@link java.text.DecimalFormat} pattern string used to format amounts
     * (e.g., {@code "#,##0.##"}). Used when {@link #isShortFormat()} is {@code false}.
     */
    String getFormatPattern();

    /**
     * Returns {@code true} if amounts should be formatted in short notation (1k, 1M, 1B, …).
     */
    boolean isShortFormat();

    /**
     * Returns {@code true} if trailing zeros after the decimal point should be stripped.
     */
    boolean isTrimZeros();

    /**
     * Returns the physical withdraw-item configuration for this currency, or {@code null}
     * if this currency has no physical form (i.e., it cannot be withdrawn as an item).
     *
     * @see XPrisonCurrencyItemConfig
     */
    default XPrisonCurrencyItemConfig getItemConfig() {
        return null;
    }

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
