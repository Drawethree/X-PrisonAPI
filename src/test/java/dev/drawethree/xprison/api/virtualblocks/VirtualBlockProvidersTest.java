package dev.drawethree.xprison.api.virtualblocks;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VirtualBlockProvidersTest {

	private final World world = mockWorld();

	private final MapBackedProvider provider = new MapBackedProvider();

	@AfterEach
	void tearDown() {
		VirtualBlockProviders.unregister(provider);
	}

	@Test
	void emptyRegistryFastPathsToNothing() {
		Location location = at(1, 2, 3);

		assertFalse(VirtualBlockProviders.hasAnyProviders());
		assertFalse(VirtualBlockProviders.isVirtualMineArea(location));
		assertNull(VirtualBlockProviders.blockAt(location));
		assertFalse(VirtualBlockProviders.hasBlockAt(location));
		assertEquals(0, VirtualBlockProviders.breakBlocks(null, List.of(location)));
		assertFalse(VirtualBlockProviders.breakBlock(null, location));
	}

	@Test
	void registeredProviderResolvesAndBreaks() {
		Location location = at(1, 2, 3);
		MineBlock block = stubBlock("DIAMOND_ORE");
		provider.put(location, block);
		VirtualBlockProviders.register(provider);

		assertTrue(VirtualBlockProviders.hasAnyProviders());
		assertTrue(VirtualBlockProviders.isVirtualMineArea(location));
		assertSame(block, VirtualBlockProviders.blockAt(location));
		assertTrue(VirtualBlockProviders.breakBlock(null, location));
		assertNull(VirtualBlockProviders.blockAt(location));
	}

	@Test
	void registeringSameProviderTwiceCountsOnce() {
		Location location = at(4, 5, 6);
		provider.put(location, stubBlock("STONE"));
		VirtualBlockProviders.register(provider);
		VirtualBlockProviders.register(provider);

		assertEquals(1, VirtualBlockProviders.breakBlocks(null, List.of(location)));
	}

	@Test
	void unregisterRemovesProvider() {
		Location location = at(1, 2, 3);
		provider.put(location, stubBlock("STONE"));
		VirtualBlockProviders.register(provider);
		VirtualBlockProviders.unregister(provider);

		assertFalse(VirtualBlockProviders.hasAnyProviders());
		assertNull(VirtualBlockProviders.blockAt(location));
	}

	@Test
	void snapshotResolvesWithoutProviderAndClosesCleanly() {
		Location location = at(7, 8, 9);
		MineBlock block = stubBlock("EMERALD_ORE");

		try (var ignored = VirtualBlockProviders.openSnapshot(Map.of(location, block))) {
			assertSame(block, VirtualBlockProviders.blockAt(location));
			assertTrue(VirtualBlockProviders.hasBlockAt(location));
		}
		assertNull(VirtualBlockProviders.blockAt(location));
	}

	@Test
	void nestedSnapshotsPopIndependently() {
		Location outerLoc = at(1, 1, 1);
		Location innerLoc = at(2, 2, 2);
		MineBlock outer = stubBlock("COAL_ORE");
		MineBlock inner = stubBlock("GOLD_ORE");

		try (var ignoredOuter = VirtualBlockProviders.openSnapshot(Map.of(outerLoc, outer))) {
			try (var ignoredInner = VirtualBlockProviders.openSnapshot(Map.of(innerLoc, inner))) {
				assertSame(outer, VirtualBlockProviders.blockAt(outerLoc));
				assertSame(inner, VirtualBlockProviders.blockAt(innerLoc));
			}
			assertSame(outer, VirtualBlockProviders.blockAt(outerLoc));
			assertNull(VirtualBlockProviders.blockAt(innerLoc));
		}
		assertNull(VirtualBlockProviders.blockAt(outerLoc));
	}

	@Test
	void captureAndOpenKeepsResolvingAfterProviderRemoval() {
		Location location = at(10, 20, 30);
		MineBlock block = stubBlock("IRON_ORE");
		provider.put(location, block);
		VirtualBlockProviders.register(provider);

		Block bukkitBlock = mock(Block.class);
		when(bukkitBlock.getType()).thenReturn(Material.AIR);
		when(bukkitBlock.getLocation()).thenReturn(location);

		try (var ignored = VirtualBlockProviders.captureAndOpen(List.of(bukkitBlock))) {
			// Simulate the pipeline removing the block through the provider mid-flight.
			VirtualBlockProviders.breakBlock(null, location);
			assertSame(block, VirtualBlockProviders.blockAt(location));
		}
		assertNull(VirtualBlockProviders.blockAt(location));
	}

	@Test
	void providerLookupsIgnoreSnapshots() {
		Location location = at(3, 4, 5);
		MineBlock block = stubBlock("REDSTONE_ORE");
		provider.put(location, block);
		VirtualBlockProviders.register(provider);

		// Simulate a pipeline: capture, then the provider removes the block.
		try (var ignored = VirtualBlockProviders.openSnapshot(Map.of(location, block))) {
			VirtualBlockProviders.breakBlock(null, location);

			// Type resolution keeps working through the snapshot...
			assertSame(block, VirtualBlockProviders.blockAt(location));
			assertTrue(VirtualBlockProviders.hasBlockAt(location));
			// ...but presence checks reflect the live store (no double-processing).
			assertNull(VirtualBlockProviders.providerBlockAt(location));
			assertFalse(VirtualBlockProviders.hasProviderBlockAt(location));
		}
	}

	@Test
	void captureAndOpenIgnoresRealBlocks() {
		Block realBlock = mock(Block.class);
		when(realBlock.getType()).thenReturn(Material.STONE);

		try (var ignored = VirtualBlockProviders.captureAndOpen(List.of(realBlock))) {
			// Nothing captured for real blocks; the world keeps answering for them.
			assertFalse(VirtualBlockProviders.hasAnyProviders());
		}
	}

	private Location at(int x, int y, int z) {
		return new Location(world, x, y, z);
	}

	private static World mockWorld() {
		World world = mock(World.class);
		when(world.getUID()).thenReturn(UUID.randomUUID());
		when(world.getName()).thenReturn("test-world");
		return world;
	}

	private static MineBlock stubBlock(String id) {
		return new MineBlock() {
			@Override
			public boolean isVanilla() {
				return true;
			}

			@Override
			public String getId() {
				return id;
			}

			@Override
			public ItemStack toItemStack(int amount) {
				return null;
			}
		};
	}

	/**
	 * Minimal provider honoring the {@link VirtualBlockProvider} contract, backed by a map.
	 */
	private static final class MapBackedProvider implements VirtualBlockProvider {

		private final Map<String, MineBlock> blocks = new HashMap<>();

		void put(Location location, MineBlock block) {
			blocks.put(key(location), block);
		}

		@Override
		public boolean isVirtualMineArea(@NotNull Location location) {
			return blocks.containsKey(key(location));
		}

		@Override
		@Nullable
		public MineBlock blockAt(@NotNull Location location) {
			return blocks.get(key(location));
		}

		@Override
		public int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations) {
			int removed = 0;
			for (Location location : locations) {
				if (blocks.remove(key(location)) != null) {
					removed++;
				}
			}
			return removed;
		}

		private static String key(Location location) {
			return location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
		}
	}
}
