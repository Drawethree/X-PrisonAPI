package dev.drawethree.xprison.api.currency.model;

import java.math.BigDecimal;

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
     * @deprecated a {@code double} cannot exactly represent OP-scale caps above ~9 quadrillion
     *             (2^53); use {@link #getMaxAmountExact()} instead.
     */
    @Deprecated
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
     * Formats an exact {@link BigDecimal} amount according to the currency's configuration.
     * Prefer this over {@link #format(double)} when the value may exceed a {@code double}'s
     * ~9 quadrillion (2^53) integer-precision limit, so the display stays exact.
     *
     * @param amount the amount to format
     * @return the formatted currency string (e.g., {@code "$1.2k"}, {@code "$3,000"})
     */
    default String format(BigDecimal amount) {
        return format(amount.doubleValue());
    }

    /**
     * Returns the initial balance assigned to a new player for this currency.
     *
     * @deprecated a {@code double} cannot exactly represent OP-scale starting amounts above
     *             ~9 quadrillion (2^53); use {@link #getStartingAmountExact()} instead.
     */
    @Deprecated
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
     * Exact-precision variant of {@link #getMaxAmount()}. Implementations that store the cap as a
     * {@link BigDecimal} override this so OP-scale caps stay exact.
     *
     * @return the maximum amount (cap) of this currency for a player
     */
    default BigDecimal getMaxAmountExact() {
        return BigDecimal.valueOf(getMaxAmount());
    }

    /**
     * Exact-precision variant of {@link #getStartingAmount()}.
     *
     * @return the initial balance assigned to a new player for this currency
     */
    default BigDecimal getStartingAmountExact() {
        return BigDecimal.valueOf(getStartingAmount());
    }

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
