package dev.drawethree.xprison.api.autosell;

import dev.drawethree.xprison.api.autosell.model.SellRegion;
import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * API interface for the AutoSell feature.
 * Provides methods for managing the selling of blocks and items,
 * setting global and regional prices, and checking player earnings.
 * <p>
 * Supports both vanilla Minecraft blocks and custom blocks/items via {@link MineBlock}.
 */
public interface XPrisonAutoSellAPI {

	/**
	 * Gets the global sell price of a specific {@link ItemStack}.
	 *
	 * @param item the item to get the price for
	 * @return the price of the item
	 */
	double getPriceForItem(ItemStack item);

	/**
	 * Gets the global sell price for a specific {@link Block}.
	 *
	 * @param block the block to get the price for
	 * @return the price of the block
	 */
	double getPriceForBlock(Block block);

	/**
	 * Sells the specified list of blocks on behalf of a player.
	 *
	 * @param player the player selling the blocks
	 * @param blocks the list of blocks to sell
	 * @return the total amount of currency earned from selling the blocks
	 */
	double sellBlocks(Player player, List<Block> blocks);

	/**
	 * Checks whether a player has auto-sell enabled.
	 *
	 * @param player the player to check
	 * @return {@code true} if auto-sell is enabled for the player, {@code false} otherwise
	 */
	boolean hasAutoSellEnabled(Player player);

	/**
	 * Adds or updates the global sell price for a specific block type.
	 * <p>
	 * The block can be either vanilla or a custom block represented by {@link MineBlock}.
	 *
	 * @param mineBlock the block to set the sell price for
	 * @param price     the price at which the block will be sold
	 */
	void addSellPrice(MineBlock mineBlock, double price);

	/**
	 * Removes a block from the global sell list.
	 *
	 * @param mineBlock the block to remove
	 */
	void removeSellPrice(MineBlock mineBlock);

	/**
	 * Gets the global sell price for a specific block type.
	 *
	 * @param mineBlock the block to check
	 * @return the sell price, or 0 if the block is not sellable
	 */
	double getSellPrice(MineBlock mineBlock);

	/**
	 * Gets the sell price of a specific {@link ItemStack} in a particular {@link SellRegion}.
	 *
	 * @param region the sell region to check pricing in
	 * @param item   the item to get the price for
	 * @return the price of the item in the specified region
	 */
	double getPriceForItem(SellRegion region, ItemStack item);

	/**
	 * Gets a collection of all currently loaded and active {@link SellRegion}s.
	 *
	 * @return a collection of all sell regions
	 */
	Collection<SellRegion> getSellRegions();

	/**
	 * Gets the sell region located at the specified {@link Location}.
	 *
	 * @param location the location to check
	 * @return the sell region at the location, or {@code null} if none exists there
	 */
	SellRegion getSellRegionAtLocation(Location location);

	/**
	 * Gets a sell region by its unique name.
	 *
	 * @param name the name of the region
	 * @return an {@link Optional} containing the sell region if found, or empty if no such region exists
	 */
	Optional<SellRegion> getSellRegionByName(String name);

	/**
	 * Returns all globally-priced blocks as a blockId → price map.
	 *
	 * @return unmodifiable map of block ID to sell price
	 */
	Map<String, Double> getGlobalPrices();

	/**
	 * Adds or updates a sell price for a specific block in the given region and persists the change.
	 *
	 * @param regionId the WorldGuard region ID
	 * @param block    the block to price
	 * @param price    the sell price (must be &gt; 0)
	 */
	void addRegionSellPrice(String regionId, MineBlock block, double price);

	/**
	 * Removes a block from the sell price list of the given region and persists the change.
	 *
	 * @param regionId the WorldGuard region ID
	 * @param block    the block to remove
	 */
	void removeRegionSellPrice(String regionId, MineBlock block);
}
