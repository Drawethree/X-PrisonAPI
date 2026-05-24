package dev.drawethree.xprison.api.autosell.model;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.Map;

/**
 * Represents a region where auto-selling blocks or items is allowed.
 * Contains methods to manage sell prices and permissions within the region.
 * <p>
 * Supports both vanilla and custom blocks via {@link MineBlock}.
 */
public interface SellRegion {

	/**
	 * Gets the WorldGuard region this SellRegion represents.
	 *
	 * @return the wrapped WorldGuard region
	 */
	IWrappedRegion getRegion();

	/**
	 * Convenience shortcut for {@code getRegion().getId()}.
	 *
	 * @return the WorldGuard region ID
	 */
	default String getId() {
		return getRegion().getId();
	}

	/**
	 * Gets the {@link World} where this region is located.
	 *
	 * @return the world of the region
	 */
	World getWorld();

	/**
	 * Gets the permission node required for a player to sell items in this region.
	 *
	 * @return the required permission string
	 */
	String getRequiredPermission();

	/**
	 * Adds or updates the sell price for a specific block type in this region.
	 * <p>
	 * The block can be either a vanilla or a custom block represented by {@link MineBlock}.
	 *
	 * @param mineBlock the block to set the sell price for
	 * @param price     the price at which the block will be sold
	 */
	void addSellPrice(MineBlock mineBlock, double price);

	/**
	 * Removes a block from being sellable in this region.
	 *
	 * @param mineBlock the block to remove from the sell price list
	 */
	void removeSellPrice(MineBlock mineBlock);

	/**
	 * Gets the sell price for a specific block in this region.
	 *
	 * @param mineBlock the block to check
	 * @return the sell price of the block, or 0 if it is not sellable
	 */
	double getSellPrice(MineBlock mineBlock);

	/**
	 * Checks whether a given {@link Location} is inside this sell region.
	 *
	 * @param location the location to check
	 * @return {@code true} if the location is inside the region, {@code false} otherwise
	 */
	boolean contains(Location location);

	/**
	 * Checks if the given player has permission to sell in this region.
	 * <p>
	 * This is based on {@link SellRegion#getRequiredPermission()}.
	 *
	 * @param player the player to check
	 * @return {@code true} if the player can sell in the region, {@code false} otherwise
	 */
	boolean canPlayerSellInRegion(Player player);

	/**
	 * Returns all priced blocks in this region as a blockId → price map.
	 *
	 * @return unmodifiable map of block ID to sell price
	 */
	Map<String, Double> getPrices();
}
