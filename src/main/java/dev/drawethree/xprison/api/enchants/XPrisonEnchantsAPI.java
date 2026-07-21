package dev.drawethree.xprison.api.enchants;

import dev.drawethree.xprison.api.enchants.area.AreaBounds;
import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

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
     * Retrieves the number of activations for a specific enchantment on the given item.
     *
     * @param item        The {@link ItemStack} to check for the enchantment's activations.
     * @param enchantment The {@link XPrisonEnchantment} whose activations are to be determined.
     * @return The total number of times the specified enchantment has been activated on the item.
     */
    long getAmountOfActivations(ItemStack item, XPrisonEnchantment enchantment);

    /**
     * Retrieves the prestige tier a specific enchantment has reached on the given item.
     * <p>
     * Addon enchants need this to apply their configured prestige reward bonus - feed the result
     * to {@link dev.drawethree.xprison.api.enchants.model.PrestigeableEnchant#getPrestigeMultiplier(int)}
     * and pass that multiplier when rewarding the player.
     *
     * @param item        The {@link ItemStack} to read, typically the player's pickaxe.
     * @param enchantment The {@link XPrisonEnchantment} whose prestige tier is wanted.
     * @return The prestige tier, or {@code 0} when the item is null or has never prestiged it.
     * @since 1.9
     */
    int getEnchantPrestige(ItemStack item, XPrisonEnchantment enchantment);

	/**
	 * Sets the number of activations recorded for a specific enchantment on the given item.
	 * <p>
	 * Counterpart to {@link #getAmountOfActivations(ItemStack, XPrisonEnchantment)}; useful for
	 * migrations or admin tooling. A value of {@code 0} clears the stored activation count.
	 *
	 * @param player      The player owning the item (used for lore refresh).
	 * @param item        The item to modify.
	 * @param enchantment The enchantment whose activation count is being set.
	 * @param amount      The new activation count (clamped at 0).
	 * @return The modified {@link ItemStack}.
	 */
	ItemStack setAmountOfActivations(Player player, ItemStack item, XPrisonEnchantment enchantment, long amount);

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

	/**
	 * Returns all enchantments currently registered in the system.
	 *
	 * @return a collection of all registered {@link XPrisonEnchantment}s
	 */
	Collection<XPrisonEnchantment> getAllEnchantments();

	/**
	 * Returns the bounds of the enchant-enabled region covering a location — the cuboid an area
	 * enchant is allowed to affect.
	 * <p>
	 * This is the enumerable counterpart to {@link #isEnchantAllowed(Location)}: where that answers
	 * "may enchants fire here?", this hands back the actual cuboid so an enchant can bound or scan
	 * its target selection (a whole-mine enchant needs the min/max corners). Where several regions
	 * overlap, the highest-priority one that permits enchants is returned.
	 * <p>
	 * The default implementation returns {@link Optional#empty()}; callers must degrade gracefully
	 * (typically by falling back to a per-block {@link #isEnchantAllowed(Location)} check).
	 *
	 * @param location a location inside the region
	 * @return the region's bounds, or empty if no enchant-enabled region covers the location
	 * @since 1.9
	 */
	default Optional<AreaBounds> getEnchantRegionBounds(Location location) {
		return Optional.empty();
	}

	/**
	 * Checks whether a block is excluded from Fortune's drop multiplication.
	 * <p>
	 * Area enchants must honour the same blacklist as the built-in Fortune enchant so a blacklisted
	 * block yields a single drop no matter how it was broken.
	 * <p>
	 * The default implementation returns {@code false} (nothing blacklisted).
	 *
	 * @param block the block to test
	 * @return {@code true} if Fortune must not multiply this block's drops
	 * @since 1.9
	 */
	default boolean isFortuneBlacklisted(Block block) {
		return false;
	}

	/**
	 * Convenience accessor for the built-in Fortune level on an item.
	 * <p>
	 * Equivalent to looking up the Fortune enchantment and calling
	 * {@link #getEnchantLevel(ItemStack, XPrisonEnchantment)}, which is what every enchant that
	 * multiplies drops needs to do.
	 *
	 * @param item the item to read, typically the player's pickaxe; {@code null} returns 0
	 * @return the Fortune level, or {@code 0} when absent, disabled or unreadable
	 * @since 1.9
	 */
	default int getItemFortuneLevel(ItemStack item) {
		if (item == null) {
			return 0;
		}
		XPrisonEnchantment fortune = getByName("Fortune");
		if (fortune == null) {
			fortune = getById(3);
		}
		if (fortune == null || !fortune.isEnabled()) {
			return 0;
		}
		return getEnchantLevel(item, fortune);
	}
}
