package dev.drawethree.xprison.api.virtualblocks;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A source of <i>virtual</i> (packet-only) mine blocks — blocks that exist in a plugin's own
 * in-memory store and on players' clients, but not in the server world (the real block at their
 * location is air).
 * <p>
 * Packet-based private-mine plugins implement this interface and register the implementation via
 * {@link VirtualBlockProviders#register(VirtualBlockProvider)}. Once registered, X-Prison's mining
 * pipeline (enchants, autosell, bombs, block counters, quests) resolves and removes virtual blocks
 * through the provider exactly as it does for real world blocks.
 *
 * <h2>Threading</h2>
 * All methods are invoked on the server main thread only.
 */
public interface VirtualBlockProvider {

	/**
	 * Checks whether the given location lies inside a packet-based mining region managed by this
	 * provider — regardless of whether a virtual block is currently present there (an already-mined
	 * position inside the region still returns {@code true}).
	 *
	 * @param location the location to test
	 * @return {@code true} if this provider manages the location
	 */
	boolean isVirtualMineArea(@NotNull Location location);

	/**
	 * Returns the virtual block currently present at the given location.
	 *
	 * @param location the location to query
	 * @return the virtual block, or {@code null} if the position is air or not managed by this
	 * provider
	 */
	@Nullable
	MineBlock blockAt(@NotNull Location location);

	/**
	 * Removes the given virtual blocks: the provider must clear them from its own store, update the
	 * clients of every viewer, and maintain its own remaining-block counters.
	 * <p>
	 * <b>Contract:</b> this method MUST NOT fire any X-Prison events and MUST NOT create drops or
	 * award currency — its callers <i>are</i> X-Prison pipelines (area enchants, bombs) that have
	 * already handled rewards. Removal must be idempotent per position: a location that is already
	 * air (or not managed by this provider) is skipped and not counted.
	 *
	 * @param cause     the player that caused the removal, or {@code null} if not caused by a player
	 * @param locations the block locations to remove
	 * @return the number of blocks actually removed
	 */
	int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations);

	/**
	 * Convenience single-block variant of {@link #breakBlocks(Player, Collection)}.
	 *
	 * @param cause    the player that caused the removal, or {@code null} if not caused by a player
	 * @param location the block location to remove
	 * @return {@code true} if a virtual block was removed
	 */
	default boolean breakBlock(@Nullable Player cause, @NotNull Location location) {
		return breakBlocks(cause, List.of(location)) > 0;
	}

	/**
	 * Counts every virtual block this provider has inside the given cuboid (inclusive), returning how
	 * many of each block type are present and — when {@code removeBlocks} is {@code true} — clearing
	 * them as it goes. Same contract as {@link #breakBlocks}: fires no X-Prison events and creates no
	 * drops.
	 * <p>
	 * Implementations resolve this from their in-memory store (a single array scan), so it is the
	 * TPS-safe path for whole-region area enchants such as Nuke — no per-block {@code getBlockAt} and
	 * no per-block world access. The default returns an empty map (provider manages nothing here).
	 *
	 * @param cause        the player that caused the collection, or {@code null}
	 * @param world        the world of the cuboid
	 * @param removeBlocks {@code true} to clear the counted blocks from the store and clients (Nuke
	 *                     that resets the mine); {@code false} to count only, leaving them intact
	 *                     (a "sell the whole mine without clearing it" enchant)
	 * @return block types mapped to their counts (empty if none)
	 */
	@NotNull
	default Map<MineBlock, Long> collectRegion(@Nullable Player cause, @NotNull World world,
											   int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
											   boolean removeBlocks) {
		return Collections.emptyMap();
	}
}
