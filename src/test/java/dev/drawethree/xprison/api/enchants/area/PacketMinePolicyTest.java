package dev.drawethree.xprison.api.enchants.area;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProvider;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProviders;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacketMinePolicyTest {

	/** A registered provider is all it takes for {@code hasAnyProviders()} to report packet mines active. */
	private static final VirtualBlockProvider STUB = new VirtualBlockProvider() {
		@Override
		public boolean isVirtualMineArea(@NotNull Location location) {
			return false;
		}

		@Override
		public @Nullable MineBlock blockAt(@NotNull Location location) {
			return null;
		}

		@Override
		public int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations) {
			return 0;
		}
	};

	@AfterEach
	void reset() {
		VirtualBlockProviders.unregister(STUB);
		PacketMinePolicy.setOptimizeForPacketMines(true);
	}

	@Test
	@DisplayName("without packet mines a PER_BLOCK enchant is left untouched")
	void noProvidersKeepsPerBlock() {
		assertEquals(BreakEventStrategy.PER_BLOCK,
				PacketMinePolicy.resolveStrategy("Explosive", BreakEventStrategy.PER_BLOCK));
	}

	@Test
	@DisplayName("with packet mines active and optimisation on, PER_BLOCK downgrades to AGGREGATE")
	void providersDowngradePerBlock() {
		VirtualBlockProviders.register(STUB);
		assertEquals(BreakEventStrategy.AGGREGATE,
				PacketMinePolicy.resolveStrategy("Explosive", BreakEventStrategy.PER_BLOCK));
	}

	@Test
	@DisplayName("with optimisation disabled the configured PER_BLOCK is honoured even on packet mines")
	void optimisationOffHonoursConfig() {
		VirtualBlockProviders.register(STUB);
		PacketMinePolicy.setOptimizeForPacketMines(false);
		assertEquals(BreakEventStrategy.PER_BLOCK,
				PacketMinePolicy.resolveStrategy("Explosive", BreakEventStrategy.PER_BLOCK));
	}

	@Test
	@DisplayName("AGGREGATE and NONE are never rewritten, packet mines or not")
	void nonPerBlockStrategiesUntouched() {
		VirtualBlockProviders.register(STUB);
		assertEquals(BreakEventStrategy.AGGREGATE,
				PacketMinePolicy.resolveStrategy("Layer", BreakEventStrategy.AGGREGATE));
		assertEquals(BreakEventStrategy.NONE,
				PacketMinePolicy.resolveStrategy("Layer", BreakEventStrategy.NONE));
	}
}
