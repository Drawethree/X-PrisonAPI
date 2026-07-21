package dev.drawethree.xprison.api.pickaxelevels;

import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeExpSource;
import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeLevel;
import dev.drawethree.xprison.api.shared.Pagination;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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

	/**
	 * Gets the pickaxe experience stored on the given pickaxe.
	 * <p>
	 * Legacy pickaxes created before the experience system report their blocks-broken count
	 * (1 block = 1 exp) until their experience tag is first written.
	 *
	 * @param pickaxe the pickaxe ItemStack to read; {@code null} or unsupported items return 0
	 * @return the pickaxe's current experience
	 */
	long getPickaxeExp(ItemStack pickaxe);

	/**
	 * Grants additional pickaxe experience to a player's pickaxe and immediately re-evaluates
	 * its level, applying any resulting level-ups and rewards.
	 * <p>
	 * Intended for addons that reward bonus pickaxe progression (e.g. a pet or booster). The gain
	 * is fired through {@link dev.drawethree.xprison.api.pickaxelevels.event.PlayerPickaxeExpGainEvent}
	 * with source {@link dev.drawethree.xprison.api.pickaxelevels.model.PickaxeExpSource#API} and is
	 * subject to the configured {@code api} source multiplier. The blocks-broken statistic is
	 * <b>not</b> affected. The {@code pickaxe} must be a supported pickaxe held by the player;
	 * otherwise this call is a no-op. The pickaxe's lore is refreshed and the (possibly upgraded)
	 * item is written back to the player's hand.
	 *
	 * @param player   the owning player
	 * @param pickaxe  the pickaxe ItemStack to progress (typically the item in the player's hand)
	 * @param extraExp additional experience to add; values {@code <= 0} are ignored
	 */
	void addPickaxeExp(Player player, ItemStack pickaxe, long extraExp);

	/**
	 * Sets the pickaxe experience to an absolute value and re-evaluates the pickaxe's level.
	 * <p>
	 * Unlike {@link #addPickaxeExp(Player, ItemStack, long)} this is an administrative write: no
	 * {@code PlayerPickaxeExpGainEvent} is fired and no source multiplier is applied. The
	 * blocks-broken statistic is not affected. The {@code pickaxe} must be a supported pickaxe
	 * held by the player; otherwise this call is a no-op.
	 *
	 * @param player  the owning player
	 * @param pickaxe the pickaxe ItemStack to modify (typically the item in the player's hand)
	 * @param exp     the absolute experience value to set; negative values are clamped to 0
	 */
	void setPickaxeExp(Player player, ItemStack pickaxe, long exp);

	/**
	 * Credits a pickaxe with both the blocks it just broke and the experience they are worth,
	 * attributed to a specific source.
	 * <p>
	 * This is the accounting call for multi-block breaks: unlike
	 * {@link #addPickaxeExp(Player, ItemStack, long)} it also advances the pickaxe's blocks-broken
	 * counter and lets the caller attribute the gain (so the configured per-source multiplier for
	 * e.g. {@link dev.drawethree.xprison.api.pickaxelevels.model.PickaxeExpSource#AREA_ENCHANTS}
	 * applies instead of the generic {@code API} source).
	 * <p>
	 * The default implementation falls back to {@link #addPickaxeExp(Player, ItemStack, long)},
	 * which awards the experience but not the blocks-broken count.
	 *
	 * @param player  the owning player
	 * @param pickaxe the pickaxe to progress, typically the item in the player's hand
	 * @param blocks  how many blocks were broken; values {@code <= 0} are ignored
	 * @param exp     how much experience those blocks are worth; values {@code <= 0} are ignored
	 * @param source  what caused the gain, for the per-source multiplier and the gain event
	 * @since 1.9
	 */
	default void addBlocksAndExp(Player player, ItemStack pickaxe, int blocks, long exp, PickaxeExpSource source) {
		addPickaxeExp(player, pickaxe, exp);
	}

	/**
	 * Sums the pickaxe experience a set of blocks is worth, honouring the configured per-block
	 * experience values.
	 * <p>
	 * Must be called while the blocks are still intact — a broken (or virtual, already-removed)
	 * block no longer resolves to a type. The default implementation returns one experience per
	 * block, matching X-Prison's behaviour when the pickaxe-levels module is disabled.
	 *
	 * @param blocks the blocks to price; {@code null} returns 0
	 * @return the total experience the blocks are worth
	 * @since 1.9
	 */
	default long getExpForBlocks(Collection<Block> blocks) {
		return blocks == null ? 0L : blocks.size();
	}

}
