package dev.drawethree.xprison.api.blocks.factory.impl;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProviders;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MineBlockFactoryImplTest {

	private final MineBlockFactoryImpl factory = new MineBlockFactoryImpl();

	/**
	 * XMaterial (used by the factory) needs a Bukkit server for version detection; provide a
	 * minimal mock once per test JVM.
	 */
	@BeforeAll
	static void bootMockServer() {
		if (Bukkit.getServer() != null) {
			return;
		}
		Server server = mock(Server.class);
		when(server.getBukkitVersion()).thenReturn("1.21.8-R0.1-SNAPSHOT");
		when(server.getVersion()).thenReturn("git-Paper (MC: 1.21.8)");
		when(server.getName()).thenReturn("Paper");
		when(server.getLogger()).thenReturn(Logger.getLogger(MineBlockFactoryImplTest.class.getName()));
		// Custom-block providers ask the plugin manager whether their plugin is enabled; the
		// default mock answers false for everything -> vanilla fallback in tests.
		when(server.getPluginManager()).thenReturn(mock(PluginManager.class));
		Bukkit.setServer(server);
	}

	@Test
	void fromBlockNullThrows() {
		assertThrows(IllegalArgumentException.class, () -> factory.fromBlock(null));
	}

	@Test
	void fromBlockAirWithoutProviderThrows() {
		Block block = airBlockAt(new Location(mockWorld(), 1, 2, 3));

		assertThrows(IllegalArgumentException.class, () -> factory.fromBlock(block));
	}

	@Test
	void fromBlockResolvesVanillaBlock() {
		Block block = mock(Block.class);
		when(block.getType()).thenReturn(Material.DIAMOND_ORE);

		MineBlock mineBlock = factory.fromBlock(block);

		assertTrue(mineBlock.isVanilla());
		assertEquals("DIAMOND_ORE", mineBlock.getId());
	}

	@Test
	void fromBlockAirResolvesThroughOpenSnapshot() {
		Location location = new Location(mockWorld(), 4, 5, 6);
		Block block = airBlockAt(location);
		MineBlock virtual = stubBlock("EMERALD_ORE");

		try (var ignored = VirtualBlockProviders.openSnapshot(Map.of(location, virtual))) {
			assertSame(virtual, factory.fromBlock(block));
		}
		assertThrows(IllegalArgumentException.class, () -> factory.fromBlock(block));
	}

	private static Block airBlockAt(Location location) {
		Block block = mock(Block.class);
		when(block.getType()).thenReturn(Material.AIR);
		when(block.getLocation()).thenReturn(location);
		return block;
	}

	private static World mockWorld() {
		World world = mock(World.class);
		when(world.getUID()).thenReturn(UUID.randomUUID());
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
}
