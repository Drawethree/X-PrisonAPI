package dev.drawethree.xprison.api.currency.model;

import dev.drawethree.xprison.api.currency.enums.LostCause;
import dev.drawethree.xprison.api.currency.enums.ReceiveCause;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

/**
 * Represents a handler for managing balance operations of a specific currency.
 * <p>
 * Implementations of this interface define how balance is stored, retrieved, and manipulated
 * for a particular currency. This allows for integration with external economy systems
 * or custom handling logic, while still interfacing with the XPrison currency API.
 */
public interface XPrisonCurrencyHandler {

    /**
     * Retrieves the current balance of the specified player for this currency.
     *
     * @param player The player whose balance should be retrieved.
     * @return The amount of currency the player currently has.
     * @deprecated a {@code double} loses integer precision above ~9 quadrillion (2^53); implement and
     *             use {@link #getBalanceExact(OfflinePlayer)} for OP-scale balances.
     */
    @Deprecated
    double getBalance(OfflinePlayer player);

    /**
     * Sets the balance of the specified player for this currency to the given amount.
     *
     * @param player The player whose balance should be set.
     * @param amount The amount to set the player's balance to.
     * @return true if the balance was successfully set, false otherwise.
     */
    boolean setBalance(OfflinePlayer player, double amount);

    /**
     * Adds the specified amount of this currency to the player's balance.
     *
     * @param player       The player to add currency to.
     * @param amount       The amount to add.
     * @param receiveCause The cause or reason for the currency gain.
     * @return true if the balance was successfully increased, false otherwise.
     */
    boolean addBalance(OfflinePlayer player, double amount, ReceiveCause receiveCause);

    /**
     * Removes the specified amount of this currency from the player's balance.
     *
     * @param player    The player to remove currency from.
     * @param amount    The amount to subtract.
     * @param lostCause The cause or reason for the currency loss.
     * @return true if the balance was successfully decreased, false otherwise.
     */
    boolean removeBalance(OfflinePlayer player, double amount, LostCause lostCause);

    /**
     * Checks whether the specified player has at least the given amount of this currency.
     *
     * @param player The player to check.
     * @param amount The minimum amount to verify.
     * @return true if the player has equal to or more than the given amount, false otherwise.
     */
    boolean has(OfflinePlayer player, double amount);

    // ------------------------------------------------------------------
    // Exact (BigDecimal) overloads. Default implementations delegate to the
    // double methods so existing handlers keep working unchanged; handlers
    // backing OP-scale balances should override these to stay exact.
    // ------------------------------------------------------------------

    /**
     * Exact-precision variant of {@link #getBalance(OfflinePlayer)}.
     *
     * @param player the player whose balance should be retrieved
     * @return the player's exact balance, never {@code null}
     */
    default BigDecimal getBalanceExact(OfflinePlayer player) {
        return BigDecimal.valueOf(getBalance(player));
    }

    /**
     * Exact-precision variant of {@link #setBalance(OfflinePlayer, double)}.
     *
     * @param player the player whose balance should be set
     * @param amount the amount to set the player's balance to
     * @return true if the balance was successfully set, false otherwise
     */
    default boolean setBalance(OfflinePlayer player, BigDecimal amount) {
        return setBalance(player, amount.doubleValue());
    }

    /**
     * Exact-precision variant of {@link #addBalance(OfflinePlayer, double, ReceiveCause)}.
     *
     * @param player       the player to add currency to
     * @param amount       the amount to add
     * @param receiveCause the cause or reason for the currency gain
     * @return true if the balance was successfully increased, false otherwise
     */
    default boolean addBalance(OfflinePlayer player, BigDecimal amount, ReceiveCause receiveCause) {
        return addBalance(player, amount.doubleValue(), receiveCause);
    }

    /**
     * Exact-precision variant of {@link #removeBalance(OfflinePlayer, double, LostCause)}.
     *
     * @param player    the player to remove currency from
     * @param amount    the amount to subtract
     * @param lostCause the cause or reason for the currency loss
     * @return true if the balance was successfully decreased, false otherwise
     */
    default boolean removeBalance(OfflinePlayer player, BigDecimal amount, LostCause lostCause) {
        return removeBalance(player, amount.doubleValue(), lostCause);
    }

    /**
     * Exact-precision variant of {@link #has(OfflinePlayer, double)}.
     *
     * @param player the player to check
     * @param amount the minimum amount to verify
     * @return true if the player has at least {@code amount}, false otherwise
     */
    default boolean has(OfflinePlayer player, BigDecimal amount) {
        return has(player, amount.doubleValue());
    }
}
