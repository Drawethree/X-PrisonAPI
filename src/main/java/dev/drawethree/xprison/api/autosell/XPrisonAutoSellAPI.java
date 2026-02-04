package dev.drawethree.xprison.api.autosell;

import com.cryptomorin.xseries.XMaterial;
import dev.drawethree.xprison.api.autosell.model.SellRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * API interface for the AutoSell feature.
 * Provides methods for managing selling blocks, pricing, and player earnings.
 */
public interface XPrisonAutoSellAPI {


	/**
	 * Gets the global price of a specific item
	 *
	 * @param item   the item to get the price for
	 * @return the price of the item
	 */
	double getPriceForItem(ItemStack item);

	/**
	 * Gets the global price for a given block.
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
	 * Adds or updates the global sell price for a specific material
	 *
	 * @param material the material to set the sell price for
	 * @param price    the price at which the material will be sold
	 */
	void addSellPrice(XMaterial material, double price);

	/**
	 * Removes a material from being sellable globally.
	 *
	 * @param material the material to remove from the sell price list
	 */
	void removeSellPrice(XMaterial material);

	/**
	 * Gets the global sell price for a specific material.
	 *
	 * @param material the material to get the price for
	 * @return the sell price of the material, or 0 if not sellable
	 */
	double getSellPriceForMaterial(XMaterial material);

    /**
     * Gets the price of a specific item in a given sell region.
     *
     * @param region the sell region to check pricing in
     * @param item   the item to get the price for
     * @return the price of the item in the specified region
     */
    double getPriceForItem(SellRegion region, ItemStack item);

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

	/**
	 * Gets the sell region by its name.
	 *
	 * @param name the name of the region to get
	 * @return the region with the given name, or {@code null} if no such region exists
	 */
	Optional<SellRegion> getSellRegionByName(String name);
}
