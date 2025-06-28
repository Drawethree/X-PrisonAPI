package dev.drawethree.xprison.api.shared.currency.enums;

import org.bukkit.OfflinePlayer;

/**
 * Represents a generic currency system in the XPrison API.
 * This interface abstracts common currency operations such as querying,
 * adding, removing, and setting amounts for players.
 *
 * Implementations could represent different currency types like Tokens, Gems, etc.
 */
public interface XPrisonCurrency {

    /**
     * Gets the current amount of this currency for the specified player.
     *
     * @param player The player (offline or online) whose balance to retrieve.
     * @return The amount of currency the player currently has.
     */
    long getAmount(OfflinePlayer player);

    /**
     * Adds a specified amount of currency to the player's balance.
     *
     * @param player The player to add currency to.
     * @param amount The amount of currency to add.
     * @param cause  The cause or reason for adding currency (e.g. mining, reward).
     */
    void add(OfflinePlayer player, long amount, ReceiveCause cause);

    /**
     * Removes a specified amount of currency from the player's balance.
     *
     * @param player The player to remove currency from.
     * @param amount The amount of currency to remove.
     * @param cause  The cause or reason for removing currency (e.g. purchase, penalty).
     */
    void remove(OfflinePlayer player, long amount, LostCause cause);

    /**
     * Sets the player's currency balance to a new specified amount.
     *
     * @param player The player whose balance to set.
     * @param amount The new amount to set.
     */
    void set(OfflinePlayer player, long amount);
}
