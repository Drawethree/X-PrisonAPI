package dev.drawethree.xprison.api.enchants.area;

import dev.drawethree.xprison.api.XPrisonAPI;
import dev.drawethree.xprison.api.autosell.XPrisonAutoSellAPI;
import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.XPrisonBlocksAPI;
import dev.drawethree.xprison.api.currency.XPrisonCurrencyAPI;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.enchants.XPrisonEnchantsAPI;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProvider;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProviders;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Covers the multi-tick area-break path: that oversized procs are spread over ticks, that spreading
 * them changes nothing a player is paid, and that two procs in flight at once cannot contaminate
 * each other.
 */
class AreaBreakChunkingTest {

	private static final int SLICE = 10;
	private static final int BLOCKS = 25;

	private final World world = mockWorld();

	private final Deque<Runnable> pendingSlices = new ArrayDeque<>();

	private final Map<Player, List<BigDecimal>> payouts = new LinkedHashMap<>();
	private final Map<Player, List<Block>> aggregated = new LinkedHashMap<>();

	private XPrisonAPI api;
	private XPrisonEnchantsAPI enchants;
	private XPrisonAutoSellAPI autoSellApi;

	private final RecordingProvider provider = new RecordingProvider();

	@BeforeEach
	void setUp() {
		this.enchants = mock(XPrisonEnchantsAPI.class);
		XPrisonBlocksAPI blocksApi = mock(XPrisonBlocksAPI.class);
		this.autoSellApi = mock(XPrisonAutoSellAPI.class);
		XPrisonCurrencyAPI currencyApi = mock(XPrisonCurrencyAPI.class);
		XPrisonCurrency currency = mock(XPrisonCurrency.class);
		this.api = mock(XPrisonAPI.class);

		when(this.api.getEnchantsApi()).thenReturn(this.enchants);
		when(this.api.getBlocksApi()).thenReturn(blocksApi);
		when(this.api.getAutoSellApi()).thenReturn(this.autoSellApi);
		when(this.api.getCurrencyApi()).thenReturn(currencyApi);

		when(this.enchants.getEnchantRegionBounds(any())).thenReturn(Optional.empty());
		when(this.enchants.isEnchantAllowed(any())).thenReturn(true);

		when(this.autoSellApi.hasAutoSellEnabled(any())).thenReturn(true);
		when(this.autoSellApi.getPriceForBlockExact(any())).thenReturn(BigDecimal.ONE);

		when(currencyApi.getCurrency("money")).thenReturn(currency);
		when(currencyApi.addBalance(any(), eq(currency), any(BigDecimal.class), any()))
				.thenAnswer(invocation -> {
					Player payee = invocation.getArgument(0);
					BigDecimal amount = invocation.getArgument(2);
					this.payouts.computeIfAbsent(payee, ignored -> new ArrayList<>()).add(amount);
					return amount;
				});

		doAnswer(invocation -> {
			Player miner = invocation.getArgument(0);
			List<Block> slice = invocation.getArgument(1);
			this.aggregated.computeIfAbsent(miner, ignored -> new ArrayList<>()).addAll(slice);
			return null;
		}).when(blocksApi).handleBlockBreak(any(), any(), anyBoolean());

		setApiInstance(this.api);
		AreaBreakChunking.setScheduler(this.pendingSlices::add);
		AreaBreakChunking.setThreshold(SLICE);
	}

	@AfterEach
	void tearDown() {
		AreaBreakChunking.setThreshold(AreaBreakChunking.DEFAULT_THRESHOLD);
		AreaBreakChunking.setScheduler(null);
		PacketMinePolicy.setOptimizeForPacketMines(true);
		VirtualBlockProviders.unregister(this.provider);
		setApiInstance(null);
	}

	@Nested
	@DisplayName("below the threshold")
	class SmallProcs {

		@Test
		@DisplayName("a small proc still resolves in a single pass, scheduling nothing")
		void smallProcIsUnchanged() {
			Player player = mockPlayer(true);
			TestContext context = new TestContext();
			List<Block> targets = realBlocks(SLICE);

			execute(context, player, targets);

			assertTrue(pendingSlices.isEmpty(), "a proc at or below the threshold must not be spread over ticks");
			assertEquals(targets, context.cleared(player));
			assertEquals(List.of(BigDecimal.valueOf(SLICE)), payouts.get(player));
			assertEquals(targets, aggregated.get(player));
			assertEquals(1, context.completions(player));
		}

		@Test
		@DisplayName("chunking can be switched off entirely, restoring the single pass at any size")
		void thresholdOfZeroDisablesChunking() {
			AreaBreakChunking.setThreshold(0);
			assertFalse(AreaBreakChunking.shouldChunk(Integer.MAX_VALUE));

			Player player = mockPlayer(true);
			TestContext context = new TestContext();
			List<Block> targets = realBlocks(BLOCKS);

			execute(context, player, targets);

			assertTrue(pendingSlices.isEmpty());
			assertEquals(targets, context.cleared(player));
		}
	}

	@Nested
	@DisplayName("above the threshold")
	class ChunkedProcs {

		@Test
		@DisplayName("the proc is spread over ticks, clearing each block exactly once")
		void everyBlockClearedExactlyOnce() {
			Player player = mockPlayer(true);
			TestContext context = new TestContext();
			List<Block> targets = realBlocks(BLOCKS);

			execute(context, player, targets);

			assertEquals(SLICE, context.cleared(player).size(), "the first slice runs in the triggering tick");
			assertEquals(1, pendingSlices.size());
			assertEquals(0, context.completions(player), "the completion hook waits for the last slice");

			drainSlices();

			assertEquals(targets, context.cleared(player));
			assertEquals(BLOCKS, new HashSet<>(context.cleared(player)).size(), "no block may be cleared twice");
			assertEquals(1, context.completions(player));
		}

		@Test
		@DisplayName("the payout happens exactly once for the whole proc, not once per slice")
		void payoutIsExactlyOnce() {
			Player player = mockPlayer(true);
			TestContext context = new TestContext();

			execute(context, player, realBlocks(BLOCKS));

			assertEquals(List.of(BigDecimal.valueOf(BLOCKS)), payouts.get(player),
					"the full earnings must be credited once, before the slices run");

			drainSlices();

			assertEquals(List.of(BigDecimal.valueOf(BLOCKS)), payouts.get(player),
					"draining the slices must not credit anything further");
		}

		@Test
		@DisplayName("the aggregate break event covers every block exactly once across the slices")
		void aggregateEventCoversEveryBlockOnce() {
			Player player = mockPlayer(true);
			TestContext context = new TestContext();
			List<Block> targets = realBlocks(BLOCKS);

			execute(context, player, targets);
			drainSlices();

			assertEquals(targets, aggregated.get(player));
		}
	}

	@Nested
	@DisplayName("two procs in flight at once")
	class Interleaving {

		/**
		 * The regression this whole design exists to prevent: enchant instances are shared between
		 * players, so a proc that survives across ticks must keep its state somewhere else. Both procs
		 * here run through the very same context instance, and their slices are drained alternately.
		 */
		@Test
		@DisplayName("procs from two players on one shared enchant never mix blocks or payouts")
		void interleavedProcsStaySeparate() {
			Player first = mockPlayer(true);
			Player second = mockPlayer(true);
			TestContext shared = new TestContext();

			List<Block> firstTargets = realBlocks(BLOCKS);
			List<Block> secondTargets = realBlocks(BLOCKS);

			execute(shared, first, firstTargets);
			execute(shared, second, secondTargets);

			while (!pendingSlices.isEmpty()) {
				pendingSlices.pollFirst().run();
			}

			assertEquals(firstTargets, shared.cleared(first));
			assertEquals(secondTargets, shared.cleared(second));
			assertEquals(firstTargets, aggregated.get(first));
			assertEquals(secondTargets, aggregated.get(second));
			assertEquals(List.of(BigDecimal.valueOf(BLOCKS)), payouts.get(first));
			assertEquals(List.of(BigDecimal.valueOf(BLOCKS)), payouts.get(second));
			assertEquals(1, shared.completions(first));
			assertEquals(1, shared.completions(second));
		}
	}

	@Nested
	@DisplayName("when the miner leaves part-way through")
	class Disconnects {

		@Test
		@DisplayName("a real-world proc finishes clearing, so the mine is never left half-eaten")
		void realWorldProcFinishesAfterDisconnect() {
			Player player = mockPlayer(true);
			TestContext context = new TestContext();
			List<Block> targets = realBlocks(BLOCKS);

			execute(context, player, targets);
			when(player.isOnline()).thenReturn(false);
			drainSlices();

			assertEquals(targets, context.cleared(player));
			assertEquals(List.of(BigDecimal.valueOf(BLOCKS)), payouts.get(player),
					"the payout landed before the slices, so leaving can neither strand nor duplicate it");
			assertEquals(0, context.completions(player), "cosmetics are not fired at a player who left");
		}

		@Test
		@DisplayName("a packet-mine proc stops, because its blocks only existed in that client")
		void virtualProcStopsAfterDisconnect() {
			PacketMinePolicy.setOptimizeForPacketMines(false);
			Player player = mockPlayer(true);
			TestContext context = new TestContext();
			List<Block> targets = virtualBlocks(BLOCKS);
			VirtualBlockProviders.register(provider);

			execute(context, player, targets);
			assertEquals(SLICE, provider.broken.size());

			when(player.isOnline()).thenReturn(false);
			drainSlices();

			assertEquals(SLICE, provider.broken.size(), "no further virtual block may be broken for an absent owner");
			assertEquals(List.of(BigDecimal.valueOf(BLOCKS)), payouts.get(player));
		}
	}

	// ------------------------------------------------------------------
	// Fixture
	// ------------------------------------------------------------------

	private void execute(TestContext context, Player player, List<Block> targets) {
		context.stage(player, targets);
		ItemStack pickaxe = player.getInventory().getItemInMainHand();
		when(this.enchants.isEnchantAnimationEnabled(player, pickaxe)).thenReturn(true);
		AreaBreakPipeline.execute(context, new BlockBreakEvent(origin(), player), 1);
	}

	private void drainSlices() {
		while (!this.pendingSlices.isEmpty()) {
			this.pendingSlices.pollFirst().run();
		}
	}

	private Block origin() {
		return block(Material.BEDROCK, -1, -1, -1);
	}

	private List<Block> realBlocks(int count) {
		return blocks(count, Material.STONE);
	}

	private List<Block> virtualBlocks(int count) {
		List<Block> created = blocks(count, Material.AIR);
		for (Block block : created) {
			this.provider.present.put(keyOf(block.getLocation()), mock(MineBlock.class));
		}
		when(this.autoSellApi.getPriceForBlockExact(any())).thenReturn(BigDecimal.ONE);
		return created;
	}

	private final Map<Material, Integer> nextY = new HashMap<>();

	private List<Block> blocks(int count, Material material) {
		List<Block> created = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			int y = this.nextY.merge(material, 1, Integer::sum);
			created.add(block(material, material.ordinal(), y, 0));
		}
		return created;
	}

	private Block block(Material material, int x, int y, int z) {
		Block block = mock(Block.class);
		when(block.getType()).thenReturn(material);
		when(block.getLocation()).thenReturn(new Location(this.world, x, y, z));
		return block;
	}

	private Player mockPlayer(boolean online) {
		Player player = mock(Player.class);
		PlayerInventory inventory = mock(PlayerInventory.class);
		ItemStack pickaxe = mock(ItemStack.class);
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.getItemInMainHand()).thenReturn(pickaxe);
		when(player.isOnline()).thenReturn(online);
		return player;
	}

	private World mockWorld() {
		World mocked = mock(World.class);
		when(mocked.getUID()).thenReturn(UUID.randomUUID());
		return mocked;
	}

	private static String keyOf(Location location) {
		return location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
	}

	private static void setApiInstance(XPrisonAPI instance) {
		try {
			Field field = XPrisonAPI.InstanceHolder.class.getDeclaredField("INSTANCE");
			field.setAccessible(true);
			field.set(null, instance);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("cannot install the test API instance", e);
		}
	}

	/**
	 * One shared context serving every player in a test, exactly as a real enchant instance does.
	 * All of its bookkeeping is keyed by player so a leak between two in-flight procs shows up as a
	 * failed assertion rather than as a passing test.
	 */
	private static final class TestContext implements AreaBreakContext {

		private final AreaBreakSettings settings =
				new AreaBreakSettings(false, BreakEventStrategy.AGGREGATE, "money", "", 0, true);

		private final Map<Player, List<Block>> staged = new LinkedHashMap<>();
		private final Map<Player, List<Block>> cleared = new LinkedHashMap<>();
		private final Map<Player, Integer> completions = new LinkedHashMap<>();

		void stage(Player player, List<Block> targets) {
			this.staged.put(player, targets);
		}

		List<Block> cleared(Player player) {
			return this.cleared.getOrDefault(player, List.of());
		}

		int completions(Player player) {
			return this.completions.getOrDefault(player, 0);
		}

		@Override
		@NotNull
		public AreaBreakSettings areaSettings() {
			return this.settings;
		}

		@Override
		@NotNull
		public String areaDisplayName() {
			return "TestArea";
		}

		@Override
		@NotNull
		public List<Block> selectTargets(Player player, Block origin, @Nullable AreaBounds region, int level) {
			return new ArrayList<>(this.staged.getOrDefault(player, List.of()));
		}

		@Override
		public void removeRealBlock(Player player, Block block) {
			this.cleared.computeIfAbsent(player, ignored -> new ArrayList<>()).add(block);
		}

		@Override
		public void onBreakComplete(Player player, List<Block> blocks) {
			this.completions.merge(player, 1, Integer::sum);
		}
	}

	/** Minimal packet-mine provider that answers for a fixed set of positions and records removals. */
	private static final class RecordingProvider implements VirtualBlockProvider {

		private final Map<String, MineBlock> present = new HashMap<>();
		private final Set<String> broken = new HashSet<>();

		@Override
		public boolean isVirtualMineArea(@NotNull Location location) {
			return true;
		}

		@Override
		@Nullable
		public MineBlock blockAt(@NotNull Location location) {
			String key = keyOf(location);
			return this.broken.contains(key) ? null : this.present.get(key);
		}

		@Override
		public int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations) {
			int removed = 0;
			for (Location location : locations) {
				String key = keyOf(location);
				if (this.present.containsKey(key) && this.broken.add(key)) {
					removed++;
				}
			}
			return removed;
		}
	}

}
