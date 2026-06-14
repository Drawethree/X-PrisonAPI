package dev.drawethree.xprison.api.pickaxelevels;

import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeLevel;
import dev.drawethree.xprison.api.shared.Pagination;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

	/**
	 * Returns all configured pickaxe levels in ascending order.
	 *
	 * @return ordered list of all defined pickaxe levels
	 */
	@NotNull
	List<PickaxeLevel> getAllPickaxeLevels();

	/**
	 * Returns the highest configured pickaxe level.
	 *
	 * @return the max pickaxe level, or {@code null} if none are configured
	 */
	@Nullable
	PickaxeLevel getMaxPickaxeLevel();

	/**
	 * Returns the top N online players by pickaxe level, ordered descending.
	 * Only online players are considered since pickaxe level is item-based, not stored in a database.
	 *
	 * @param limit maximum number of entries to return
	 * @return ordered map of UUID → pickaxe level number
	 */
	@NotNull
	Map<UUID, Integer> getTopByPickaxeLevel(int limit);

	/**
	 * Paginated variant of {@link #getTopByPickaxeLevel(int)}.
	 * <p>
	 * The default implementation over-fetches and slices the ordered result client-side.
	 *
	 * @param limit  maximum number of entries to return
	 * @param offset number of leading entries to skip (0 = start from top)
	 * @return ordered map of UUID → pickaxe level number
	 */
	@NotNull
	default Map<UUID, Integer> getTopByPickaxeLevel(int limit, int offset) {
		return Pagination.slice(getTopByPickaxeLevel(limit + Math.max(offset, 0)), limit, offset);
	}

}
