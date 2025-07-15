package dev.drawethree.xprison.api.enchants;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * The {@code XPrisonEnchantsAPI} interface defines methods for interacting with and managing
 * custom enchantments in the XPrison plugin. It provides utilities to query, modify, register,
 * and remove custom enchantments on {@link ItemStack} objects as well as control enchantment-related
 * behavior during block-breaking events.
 *
 * <p>This API is intended for developers who wish to interact with the XPrison enchantment system.
 */
public interface XPrisonEnchantsAPI {

	/**
	 * Retrieves all custom enchantments applied to a specific item.
	 *
	 * @param itemStack The item to check for enchantments.
	 * @return A map of {@link XPrisonEnchantment} to their respective levels on the item.
	 */
	Map<XPrisonEnchantment, Integer> getEnchants(ItemStack itemStack);

	/**
	 * Checks if an item has a specific enchantment applied.
	 *
	 * @param item        The item to check.
	 * @param enchantment The enchantment to look for.
	 * @return {@code true} if the item has the specified enchantment, {@code false} otherwise.
	 */
	boolean hasEnchant(ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Checks if an item has a specific enchantment at a given level.
	 *
	 * @param item        The item to check.
	 * @param enchantment The enchantment to look for.
	 * @param level       The required level of the enchantment.
	 * @return {@code true} if the item has the specified enchantment at the given level, {@code false} otherwise.
	 */
	boolean hasEnchant(ItemStack item, XPrisonEnchantment enchantment, int level);

	/**
	 * Gets the level of a specific enchantment on an item.
	 *
	 * @param item        The item to inspect.
	 * @param enchantment The enchantment whose level is to be retrieved.
	 * @return The level of the enchantment, or {@code 0} if not present.
	 */
	int getEnchantLevel(ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Sets or updates a specific enchantment with a defined level on an item.
	 *
	 * @param player      The player applying the enchantment.
	 * @param item        The item to be enchanted.
	 * @param enchantment The enchantment to apply.
	 * @param level       The level to set.
	 * @return The modified {@link ItemStack} with the enchantment applied.
	 */
	ItemStack setEnchantLevel(Player player, ItemStack item, XPrisonEnchantment enchantment, int level);

	/**
	 * Removes a specific enchantment from an item.
	 *
	 * @param player      The player performing the removal.
	 * @param item        The item to modify.
	 * @param enchantment The enchantment to remove.
	 * @return The modified {@link ItemStack} with the enchantment removed.
	 */
	ItemStack removeEnchant(Player player, ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Retrieves a custom enchantment by its internal ID.
	 *
	 * @param id The unique ID of the enchantment.
	 * @return The corresponding {@link XPrisonEnchantment}, or {@code null} if not found.
	 */
	XPrisonEnchantment getById(int id);

	/**
	 * Retrieves a custom enchantment by its raw (unformatted) name.
	 *
	 * @param rawName The raw name of the enchantment.
	 * @return The corresponding {@link XPrisonEnchantment}, or {@code null} if not found.
	 */
	XPrisonEnchantment getByName(String rawName);

	/**
	 * Registers a new custom enchantment to the system.
	 *
	 * @param enchantment The enchantment to register.
	 * @return {@code true} if registration was successful, {@code false} otherwise.
	 */
	boolean registerEnchant(XPrisonEnchantment enchantment);

	/**
	 * Unregisters an existing custom enchantment.
	 *
	 * @param enchantment The enchantment to unregister.
	 * @return {@code true} if unregistration was successful, {@code false} otherwise.
	 */
	boolean unregisterEnchant(XPrisonEnchantment enchantment);

	/**
	 * Prevents the specified {@link BlockBreakEvent} from triggering any enchantment logic.
	 * Useful for temporary suppression of enchantment effects.
	 *
	 * @param event The block break event to ignore.
	 */
	void ignoreBlockBreakEvent(BlockBreakEvent event);

	/**
	 * Checks whether enchantments are allowed at a given {@link Location}.
	 *
	 * @param location The location to check.
	 * @return {@code true} if enchantments are allowed, {@code false} otherwise.
	 */
	boolean isEnchantAllowed(Location location);
}
