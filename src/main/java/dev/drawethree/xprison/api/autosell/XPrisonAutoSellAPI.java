package dev.drawethree.xprison.api.autosell;

import dev.drawethree.xprison.api.autosell.model.SellRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

/**
 * API interface for the AutoSell feature.
 * Provides methods for managing selling blocks, pricing, and player earnings.
 */
public interface XPrisonAutoSellAPI {

	/**
	 * Gets the current total earnings of a player from auto-selling.
	 *
	 * @param player the player whose earnings to retrieve
	 * @return the current earnings of the player
	 */
	double getCurrentEarnings(Player player);

	/**
	 * Gets the price of a specific item in a given sell region.
	 *
	 * @param region the sell region to check pricing in
	 * @param item   the item to get the price for
	 * @return the price of the item in the specified region
	 */
	double getPriceForItem(SellRegion region, ItemStack item);

	/**
	 * Gets the price for a given block.
	 *
	 * @param block the block to get the price for
	 * @return the price of the block
	 */
	double getPriceForBlock(Block block);

	/**
	 * Sells the specified list of blocks on behalf of the player.
	 *
	 * @param player the player selling the blocks
	 * @param blocks the list of blocks to sell
	 */
	void sellBlocks(Player player, List<Block> blocks);

	/**
	 * Checks if a player has auto-sell enabled.
	 *
	 * @param p the player to check
	 * @return {@code true} if auto-sell is enabled for the player, {@code false} otherwise
	 */
	boolean hasAutoSellEnabled(Player p);

	/**
	 * Gets a collection of all loaded and active sell regions.
	 *
	 * @return a collection of all sell regions
	 */
	Collection<SellRegion> getSellRegions();

	/**
	 * Gets the sell region at a specified location.
	 *
	 * @param location the location to check
	 * @return the sell region at the given location, or {@code null} if none exists there
	 */
	SellRegion getSellRegionAtLocation(Location location);
}
