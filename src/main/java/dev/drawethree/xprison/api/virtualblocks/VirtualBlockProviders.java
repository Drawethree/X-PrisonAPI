package dev.drawethree.xprison.api.virtualblocks;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry of the {@link VirtualBlockProvider}s currently plugged into X-Prison, and the single
 * entry point through which the mining pipeline resolves and removes virtual (packet-only) blocks.
 * <p>
 * Mirrors the {@link dev.drawethree.xprison.api.blocks.provider.CustomBlockProviders} pattern: a
 * static registry, because the mine block factory is instantiated statically in several core
 * classes and cannot reach an instance-scoped seam.
 * <p>
 * When no provider is registered and no snapshot is open, every query returns
 * {@code null}/{@code false}/{@code 0} without further work — core call sites are guaranteed to be
 * behaviorally inert on servers without a packet-mines plugin.
 *
 * <h2>Snapshots</h2>
 * X-Prison's break pipelines read block types <i>after</i> removing the blocks (area enchants,
 * bombs, the aggregate {@code XPrisonBlockBreakEvent} consumed by quests). For real blocks that is
 * a benign pre-existing quirk; for virtual blocks the type would be lost the moment the provider
 * removes them. A pipeline therefore opens a <b>snapshot</b> capturing the virtual types it is
 * about to remove; while the snapshot is open (on the same thread), {@link #blockAt(Location)}
 * keeps resolving those positions. Snapshots nest (stack semantics) and must be closed via
 * try-with-resources.
 *
 * <h2>Threading</h2>
 * Registration may happen during plugin enable/disable; queries and snapshots are main-thread only
 * (snapshots are thread-local by design).
 */
public final class VirtualBlockProviders {

	private static final CopyOnWriteArrayList<VirtualBlockProvider> PROVIDERS = new CopyOnWriteArrayList<>();

	private static final ThreadLocal<ArrayDeque<Map<PosKey, MineBlock>>> SNAPSHOTS =
			ThreadLocal.withInitial(ArrayDeque::new);

	private VirtualBlockProviders() {
	}

	/**
	 * Registers a provider. Registering the same instance twice has no effect.
	 *
	 * @param provider the provider to register
	 */
	public static void register(@NotNull VirtualBlockProvider provider) {
		PROVIDERS.addIfAbsent(provider);
	}

	/**
	 * Unregisters a previously registered provider.
	 *
	 * @param provider the provider to remove
	 */
	public static void unregister(@NotNull VirtualBlockProvider provider) {
		PROVIDERS.remove(provider);
	}

	/**
	 * Fast-path gate used by core call sites: {@code false} guarantees that no virtual blocks exist
	 * anywhere on this server.
	 *
	 * @return {@code true} if at least one provider is registered
	 */
	public static boolean hasAnyProviders() {
		return !PROVIDERS.isEmpty();
	}

	/**
	 * Checks whether the location lies inside any registered provider's packet-based mining region.
	 *
	 * @param location the location to test
	 * @return {@code true} if some provider manages the location
	 */
	public static boolean isVirtualMineArea(@NotNull Location location) {
		for (VirtualBlockProvider provider : PROVIDERS) {
			if (provider.isVirtualMineArea(location)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resolves the virtual block at a location. Open snapshots on the current thread are consulted
	 * first (top-down), then the registered providers.
	 *
	 * @param location the location to query
	 * @return the virtual block, or {@code null} if none
	 */
	@Nullable
	public static MineBlock blockAt(@NotNull Location location) {
		ArrayDeque<Map<PosKey, MineBlock>> snapshots = SNAPSHOTS.get();
		if (!snapshots.isEmpty()) {
			PosKey key = PosKey.of(location);
			for (Map<PosKey, MineBlock> snapshot : snapshots) {
				MineBlock block = snapshot.get(key);
				if (block != null) {
					return block;
				}
			}
		}
		for (VirtualBlockProvider provider : PROVIDERS) {
			MineBlock block = provider.blockAt(location);
			if (block != null) {
				return block;
			}
		}
		return null;
	}

	/**
	 * @param location the location to query
	 * @return {@code true} if a virtual block is present (or snapshotted) at the location
	 */
	public static boolean hasBlockAt(@NotNull Location location) {
		return blockAt(location) != null;
	}

	/**
	 * Resolves the virtual block at a location from the <b>live providers only</b>, ignoring open
	 * snapshots. Use this for <i>presence</i> decisions ("is there still a block to sell/break
	 * here?") — a snapshot deliberately keeps already-removed blocks resolving for type reads, so
	 * consulting it for presence would double-process blocks a pipeline already handled.
	 *
	 * @param location the location to query
	 * @return the virtual block, or {@code null} if none
	 */
	@Nullable
	public static MineBlock providerBlockAt(@NotNull Location location) {
		for (VirtualBlockProvider provider : PROVIDERS) {
			MineBlock block = provider.blockAt(location);
			if (block != null) {
				return block;
			}
		}
		return null;
	}

	/**
	 * @param location the location to query
	 * @return {@code true} if a virtual block is currently present in a live provider's store
	 * (snapshots ignored — see {@link #providerBlockAt})
	 */
	public static boolean hasProviderBlockAt(@NotNull Location location) {
		return !PROVIDERS.isEmpty() && providerBlockAt(location) != null;
	}

	/**
	 * Removes virtual blocks through the registered providers. See
	 * {@link VirtualBlockProvider#breakBlocks(Player, Collection)} for the removal contract.
	 *
	 * @param cause     the player that caused the removal, or {@code null}
	 * @param locations the locations to remove
	 * @return the total number of blocks actually removed
	 */
	public static int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations) {
		int removed = 0;
		for (VirtualBlockProvider provider : PROVIDERS) {
			removed += provider.breakBlocks(cause, locations);
		}
		return removed;
	}

	/**
	 * Convenience single-block variant of {@link #breakBlocks(Player, Collection)}.
	 *
	 * @param cause    the player that caused the removal, or {@code null}
	 * @param location the location to remove
	 * @return {@code true} if a virtual block was removed
	 */
	public static boolean breakBlock(@Nullable Player cause, @NotNull Location location) {
		return breakBlocks(cause, List.of(location)) > 0;
	}

	/**
	 * Counts every virtual block inside the cuboid through the provider that owns it, optionally
	 * clearing them, and returns how many of each block type were found. See
	 * {@link VirtualBlockProvider#collectRegion} — this is the TPS-safe whole-region path for area
	 * enchants (Nuke) that resolves straight from the provider's store instead of scanning the world
	 * block-by-block.
	 *
	 * @param removeBlocks {@code true} to clear the counted blocks (mine-resetting Nuke); {@code false}
	 *                     to count only, for a "sell without clearing" enchant
	 * @return block types mapped to their counts (empty if no provider owns the cuboid)
	 */
	@NotNull
	public static Map<MineBlock, Long> collectRegion(@Nullable Player cause, @NotNull World world,
													 int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
													 boolean removeBlocks) {
		for (VirtualBlockProvider provider : PROVIDERS) {
			Map<MineBlock, Long> found = provider.collectRegion(cause, world, minX, minY, minZ, maxX, maxY, maxZ, removeBlocks);
			if (!found.isEmpty()) {
				return found;
			}
		}
		return Map.of();
	}

	/**
	 * Captures the virtual types of the given blocks (positions that are server-side air but
	 * resolve through a provider or an already-open snapshot) and opens a snapshot overlay with
	 * them, so they keep resolving for the duration of one break pipeline even after the provider
	 * removes them.
	 * <p>
	 * Real (non-air) blocks are ignored — the world itself keeps answering for them.
	 *
	 * @param blocks the blocks about to be processed by a break pipeline
	 * @return a handle that must be closed (try-with-resources) when the pipeline finishes
	 */
	@NotNull
	public static SnapshotHandle captureAndOpen(@NotNull Collection<Block> blocks) {
		// Zero-overhead fast path: with no provider registered and no snapshot open, nothing
		// virtual can exist, so the whole capture scan (one iteration over every affected block —
		// e.g. an entire Nuke region) is skipped. This keeps AOE enchants / bombs byte-for-byte
		// as cheap as before packet-mines existed when the feature is off.
		if (PROVIDERS.isEmpty() && SNAPSHOTS.get().isEmpty()) {
			return NO_OP_HANDLE;
		}
		Map<PosKey, MineBlock> captured = new HashMap<>();
		for (Block block : blocks) {
			if (!isAir(block.getType())) {
				continue;
			}
			Location location = block.getLocation();
			MineBlock virtual = blockAt(location);
			if (virtual != null) {
				captured.put(PosKey.of(location), virtual);
			}
		}
		return open(captured);
	}

	/**
	 * Opens a snapshot overlay from already-resolved virtual blocks. Used by packet-mines plugins
	 * that know the removed block's type up front (e.g. around a synthetic
	 * {@link org.bukkit.event.block.BlockBreakEvent} for a block they removed themselves).
	 *
	 * @param resolved the positions and their virtual types
	 * @return a handle that must be closed (try-with-resources) when the pipeline finishes
	 */
	@NotNull
	public static SnapshotHandle openSnapshot(@NotNull Map<Location, MineBlock> resolved) {
		Map<PosKey, MineBlock> keyed = new HashMap<>(resolved.size());
		for (Map.Entry<Location, MineBlock> entry : resolved.entrySet()) {
			keyed.put(PosKey.of(entry.getKey()), entry.getValue());
		}
		return open(keyed);
	}

	/**
	 * Air check by constant comparison — equivalent to {@link org.bukkit.Material#isAir()} for
	 * block materials, without touching the Bukkit registry (which requires a running server).
	 */
	private static boolean isAir(org.bukkit.Material material) {
		return material == org.bukkit.Material.AIR
				|| material == org.bukkit.Material.CAVE_AIR
				|| material == org.bukkit.Material.VOID_AIR;
	}

	private static SnapshotHandle open(Map<PosKey, MineBlock> snapshot) {
		ArrayDeque<Map<PosKey, MineBlock>> stack = SNAPSHOTS.get();
		stack.push(snapshot);
		return () -> {
			ArrayDeque<Map<PosKey, MineBlock>> current = SNAPSHOTS.get();
			current.remove(snapshot);
			if (current.isEmpty()) {
				SNAPSHOTS.remove();
			}
		};
	}

	/**
	 * A handle to an open snapshot overlay; closing it removes the overlay. Closing is idempotent
	 * only in the sense that a snapshot already removed is silently skipped — always use
	 * try-with-resources.
	 */
	@FunctionalInterface
	public interface SnapshotHandle extends AutoCloseable {
		@Override
		void close();
	}

	/** Shared no-op handle returned by the {@link #captureAndOpen} fast path (nothing was pushed). */
	private static final SnapshotHandle NO_OP_HANDLE = () -> {
	};

	/**
	 * Immutable block-position key (world id + block coordinates).
	 */
	private record PosKey(UUID worldId, int x, int y, int z) {

		static PosKey of(Location location) {
			World world = location.getWorld();
			UUID worldId = world != null ? world.getUID() : null;
			return new PosKey(worldId, location.getBlockX(), location.getBlockY(), location.getBlockZ());
		}
	}
}
