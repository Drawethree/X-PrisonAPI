package dev.drawethree.xprison.api.pickaxelevels;

import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeLevel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * API for managing pickaxe levels in XPrison.
 */
public interface XPrisonPickaxeLevelsAPI {

	/**
	 * Gets the pickaxe level associated with the given item stack.
	 *
	 * @param item the ItemStack to check
	 * @return an Optional containing the PickaxeLevel if present, otherwise empty
	 */
	Optional<PickaxeLevel> getPickaxeLevel(ItemStack item);

	/**
	 * Gets the pickaxe level of a specific player.
	 *
	 * @param player the player whose pickaxe level to get
	 * @return an Optional containing the PickaxeLevel if present, otherwise empty
	 */
	Optional<PickaxeLevel> getPickaxeLevel(Player player);

	/**
	 * Gets the pickaxe level by its numeric level.
	 *
	 * @param level the level to retrieve
	 * @return an Optional containing the PickaxeLevel if found, otherwise empty
	 */
	Optional<PickaxeLevel> getPickaxeLevel(int level);

	/**
	 * Sets the pickaxe level on an item stack for a player.
	 *
	 * @param player the player owning the item
	 * @param item the ItemStack to modify
	 * @param level the PickaxeLevel to set
	 */
	void setPickaxeLevel(Player player, ItemStack item, PickaxeLevel level);

	/**
	 * Sets the pickaxe level on an item stack for a player by level number.
	 *
	 * @param player the player owning the item
	 * @param item the ItemStack to modify
	 * @param level the numeric level to set
	 */
	void setPickaxeLevel(Player player, ItemStack item, int level);

	/**
	 * Gets the progress bar string representing the player's pickaxe level progress.
	 *
	 * @param player the player to get the progress bar for
	 * @return a string representing the progress bar
	 */
	String getProgressBar(Player player);

}
