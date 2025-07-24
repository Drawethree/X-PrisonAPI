package dev.drawethree.xprison.api.currency;

import dev.drawethree.xprison.api.currency.enums.LostCause;
import dev.drawethree.xprison.api.currency.enums.ReceiveCause;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

public interface XPrisonCurrencyAPI {

    /**
     * Registers a new custom currency to the system.
     * If a currency with the same name (case-insensitive) already exists, it will be overwritten.
     *
     * @param currency The {@link XPrisonCurrency} implementation to register.
     */
    void registerCurrency(XPrisonCurrency currency);

    /**
     * Unregisters a currency from the system.
     * Player balances and related data for the currency may be removed or archived.
     *
     * @param currency The {@link XPrisonCurrency} implementation to unregister.
     */
    void unregisterCurrency(XPrisonCurrency currency);

    /**
     * Retrieves a currency by its name.
     *
     * @param name The name of the currency (case-insensitive).
     * @return The matching {@link XPrisonCurrency}, or null if not found.
     */
    XPrisonCurrency getCurrency(String name);

    /**
     * Returns all registered currencies.
     *
     * @return A collection of all {@link XPrisonCurrency} objects.
     */
    Collection<XPrisonCurrency> getAllCurrencies();

    /**
     * Gets the current amount of a specific currency for the specified player.
     *
     * @param player The player whose balance to retrieve.
     * @param currencyName The currency to check.
     * @return The amount of currency the player currently has.
     */
    double getBalance(OfflinePlayer player, String currencyName);

    /**
     * Adds a specified amount of a specific currency to the player's balance.
     *
     * @param player The player to add currency to.
     * @param currencyName The currency to add.
     * @param amount The amount to add.
     * @param receiveCause The reason for receiving the currency.
     * @return true if added successfully, false otherwise.
     */
    boolean addBalance(OfflinePlayer player, String currencyName, double amount, ReceiveCause receiveCause);

    /**
     * Removes a specified amount of a specific currency from the player's balance.
     *
     * @param player The player to remove currency from.
     * @param currencyName The currency to remove.
     * @param amount The amount to remove.
     * @param lostCause The reason for removing the currency.
     * @return true if removed successfully, false otherwise.
     */
    boolean removeBalance(OfflinePlayer player, String currencyName, double amount, LostCause lostCause);

    /**
     * Sets the balance of a specific currency for the player.
     *
     * @param player The player whose balance to set.
     * @param currencyName The currency to set.
     * @param amount The amount to set.
     * @return true if set successfully, false otherwise.
     */
    boolean setBalance(OfflinePlayer player, String currencyName, double amount);

    /**
     * Checks if the player has at least the specified amount of the given currency.
     *
     * @param player The player to check.
     * @param currencyName The currency to check.
     * @param amount The minimum amount required.
     * @return true if player has enough, false otherwise.
     */
    boolean has(OfflinePlayer player, String currencyName, double amount);
}
