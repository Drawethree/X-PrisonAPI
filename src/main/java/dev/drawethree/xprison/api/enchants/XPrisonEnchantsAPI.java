package dev.drawethree.xprison.api.enchants;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * API interface for managing custom enchantments on items.
 */
public interface XPrisonEnchantsAPI {

	/**
	 * Retrieves all custom enchantments applied to a specific item.
	 *
	 * @param itemStack The item to check.
	 * @return A map of {@link XPrisonEnchantment} to their respective levels on the item.
	 */
	Map<XPrisonEnchantment, Integer> getEnchants(ItemStack itemStack);

	/**
	 * Checks if an item has a specific enchantment.
	 *
	 * @param item        The item to check.
	 * @param enchantment The enchantment to look for.
	 * @return True if the item has the specified enchantment, false otherwise.
	 */
	boolean hasEnchant(ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Checks if an item has a specific enchantment at a given level.
	 *
	 * @param item        The item to check.
	 * @param enchantment The enchantment to look for.
	 * @param level       The specific enchantment level.
	 * @return True if the item has the specified enchantment at the given level, false otherwise.
	 */
	boolean hasEnchant(ItemStack item, XPrisonEnchantment enchantment, int level);

	/**
	 * Gets the level of a specific enchantment on an item.
	 *
	 * @param item        The item to check.
	 * @param enchantment The enchantment to retrieve the level for.
	 * @return The level of the enchantment, or 0 if the enchantment is not present.
	 */
	int getEnchantLevel(ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Sets an enchantment with a specific level on an item.
	 *
	 * @param player      The player applying the enchantment.
	 * @param item        The item to enchant.
	 * @param enchantment The enchantment to apply.
	 * @param level       The level of the enchantment.
	 * @return The modified item with the enchantment applied.
	 */
	ItemStack setEnchantLevel(Player player, ItemStack item, XPrisonEnchantment enchantment, int level);

	/**
	 * Removes a specific enchantment from an item.
	 *
	 * @param player      The player removing the enchantment.
	 * @param item        The item to modify.
	 * @param enchantment The enchantment to remove.
	 * @return The modified item with the enchantment removed.
	 */
	ItemStack removeEnchant(Player player, ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Retrieves an enchantment by its ID.
	 *
	 * @param id The ID of the enchantment.
	 * @return The {@link XPrisonEnchantment} corresponding to the ID, or null if not found.
	 */
	XPrisonEnchantment getById(int id);

	/**
	 * Retrieves an enchantment by its raw name.
	 *
	 * @param rawName The raw name of the enchantment.
	 * @return The {@link XPrisonEnchantment} corresponding to the name, or null if not found.
	 */
	XPrisonEnchantment getByName(String rawName);

	/**
	 * Registers a new custom enchantment.
	 *
	 * @param enchantment The enchantment to register.
	 * @return True if the enchantment was registered successfully, false otherwise.
	 */
	boolean registerEnchant(XPrisonEnchantment enchantment);

	/**
	 * Unregisters an existing custom enchantment.
	 *
	 * @param enchantment The enchantment to unregister.
	 * @return True if the enchantment was unregistered successfully, false otherwise.
	 */
	boolean unregisterEnchant(XPrisonEnchantment enchantment);

}
