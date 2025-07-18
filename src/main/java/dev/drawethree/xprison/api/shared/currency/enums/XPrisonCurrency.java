package dev.drawethree.xprison.api.shared.currency.enums;

import org.bukkit.OfflinePlayer;

/**
 * Represents a generic currency system in the XPrison API.
 * <p>
 * This interface abstracts common currency operations such as querying,
 * adding, removing, and setting balances for players. Implementations
 * could represent different currency types like Tokens, Gems, Money, etc.
 * <p>
 * Methods that modify balances return a boolean indicating whether the
 * operation was successful (e.g., not blocked, not insufficient funds, etc.).
 */
public interface XPrisonCurrency {

    /**
     * Gets the name of this currency.
     * <p>
     * This is typically used for display purposes, configuration keys,
     * or to distinguish between multiple currency types (e.g., "Tokens", "Gems", "Money").
     *
     * @return The name of the currency.
     */
    String getName();

    /**
     * Gets the current amount of this currency for the specified player.
     *
     * @param player The player (offline or online) whose balance to retrieve.
     * @return The amount of currency the player currently has.
     */
    double getBalance(OfflinePlayer player);

    /**
     * Adds a specified amount of currency to the player's balance.
     *
     * @param player The player to add currency to.
     * @param amount The amount of currency to add.
     * @return true if the currency was successfully added, false otherwise.
     */
    boolean addBalance(OfflinePlayer player, double amount);

    /**
     * Removes a specified amount of currency from the player's balance.
     *
     * @param player The player to remove currency from.
     * @param amount The amount of currency to remove.
     * @return true if the currency was successfully removed, false (e.g., insufficient funds) otherwise.
     */
    boolean removeBalance(OfflinePlayer player, double amount);

    /**
     * Sets the player's currency balance to a new specified amount.
     *
     * @param player The player whose balance to set.
     * @param amount The new amount to set.
     * @return true if the balance was successfully set, false otherwise.
     */
    boolean setBalance(OfflinePlayer player, double amount);

    /**
     * Checks if the specified player has at least the given amount of this currency.
     *
     * @param player The player whose balance to check.
     * @param amount The minimum amount to verify.
     * @return true if the player has at least the specified amount, false otherwise.
     */
    boolean has(OfflinePlayer player, double amount);
}
