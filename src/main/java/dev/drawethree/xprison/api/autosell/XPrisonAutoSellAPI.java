package dev.drawethree.xprison.api.autosell;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * API interface for the AutoSell feature.
 * Provides methods for managing selling blocks, pricing, and player earnings.
 */
public interface XPrisonAutoSellAPI {


	/**
	 * Gets the price of a specific item
	 *
	 * @param item   the item to get the price for
	 * @return the price of the item
	 */
	double getPriceForItem(ItemStack item);

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
	 * Adds or updates the sell price for a specific material
	 *
	 * @param material the material to set the sell price for
	 * @param price    the price at which the material will be sold
	 */
	void addSellPrice(XMaterial material, double price);

	/**
	 * Removes a material from being sellable in this region.
	 *
	 * @param material the material to remove from the sell price list
	 */
	void removeSellPrice(XMaterial material);

	/**
	 * Gets the sell price for a specific material in this region.
	 *
	 * @param material the material to get the price for
	 * @return the sell price of the material, or 0 if not sellable
	 */
	double getSellPriceForMaterial(XMaterial material);
}
