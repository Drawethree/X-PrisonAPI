package dev.drawethree.xprison.api.enchants.area;

import dev.drawethree.xprison.api.XPrisonAPI;
import dev.drawethree.xprison.api.autosell.XPrisonAutoSellAPI;
import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.XPrisonBlocksAPI;
import dev.drawethree.xprison.api.blocks.factory.MineBlockFactory;
import dev.drawethree.xprison.api.blocks.factory.impl.MineBlockFactoryImpl;
import dev.drawethree.xprison.api.currency.XPrisonCurrencyAPI;
import dev.drawethree.xprison.api.currency.enums.ReceiveCause;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.enchants.XPrisonEnchantsAPI;
import dev.drawethree.xprison.api.mines.XPrisonMinesAPI;
import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.pickaxelevels.XPrisonPickaxeLevelsAPI;
import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeExpSource;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProviders;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The single implementation of X-Prison's area-break behaviour: region gating, target filtering,
 * event dispatch, Fortune-multiplied drops or auto-sell earnings, block clearing, mine-reset
 * accounting, pickaxe progression, prestige-scaled payout and the proc message — on normal world
 * mines and on packet ("virtual") mines alike.
 * <p>
 * Shared by composition so both X-Prison's bundled enchants and addon enchants built on
 * {@link AreaBreakEnchant} execute exactly the same logic; see {@link AreaBreakContext}.
 *
 * <h2>Optional modules</h2>
 * Every module-backed sub-API is treated as optional. A server may run with Mines, AutoSell, Blocks,
 * Pickaxe Levels or Currency switched off, and each feature simply drops out of the pipeline rather
 * than breaking the enchant.
 *
 * <h2>Oversized procs</h2>
 * A proc is normally resolved in one synchronous pass. Above {@link AreaBreakChunking#threshold()}
 * the rewards are still settled in that one pass, but clearing the blocks, advancing the mine and
 * firing the aggregate break event are spread over consecutive ticks - see
 * {@link AreaBreakChunking}.
 *
 * @since 1.9
 */
public final class AreaBreakPipeline {

	private AreaBreakPipeline() {
	}

	/**
	 * Runs the whole area-break pipeline for one trigger.
	 *
	 * @param context the enchant's settings, target selection and policies
	 * @param event   the break that triggered the enchant
	 * @param level   the enchantment level on the pickaxe
	 */
	public static void execute(@NotNull AreaBreakContext context, @NotNull BlockBreakEvent event, int level) {
		final XPrisonAPI api = XPrisonAPI.getInstance();
		final XPrisonEnchantsAPI enchants = optional(api::getEnchantsApi);
		if (enchants == null) {
			return; // enchants module is off; nothing should be firing at all
		}

		final Player player = event.getPlayer();
		final Block origin = event.getBlock();
		final Location originLocation = origin.getLocation();

		AreaBounds region = enchants.getEnchantRegionBounds(originLocation).orElse(null);
		if (region == null && !enchants.isEnchantAllowed(originLocation)) {
			return;
		}

		// Captured now: an animated enchant may resolve many ticks later, by which time the player
		// could be holding something else.
		final ItemStack pickaxe = player.getInventory().getItemInMainHand();

		// Per-pickaxe "Enchant Animations" setting. When off, the deferred cosmetic effect is bypassed
		// (resolve runs immediately) and the completion hook is skipped, for every area enchant.
		final boolean animations = enchants.isEnchantAnimationEnabled(player, pickaxe);

		if (region != null && context.breaksEntireRegion() && VirtualBlockProviders.isVirtualMineArea(originLocation)) {
			context.onBreakStart(player, origin, level);
			if (animations) {
				context.dispatchWithEffect(player, origin, Collections.emptyList(), level,
						ignoredTargets -> resolveEntireVirtualRegion(api, context, player, pickaxe, region, true));
			} else {
				resolveEntireVirtualRegion(api, context, player, pickaxe, region, false);
			}
			return;
		}

		List<Block> targets = filterTargets(enchants, context, context.selectTargets(player, origin, region, level), origin, region);
		if (targets.isEmpty()) {
			return;
		}

		context.onBreakStart(player, origin, level);
		if (animations) {
			context.dispatchWithEffect(player, origin, targets, level,
					finalTargets -> resolve(api, context, player, pickaxe, origin, region, finalTargets, true));
		} else {
			resolve(api, context, player, pickaxe, origin, region, targets, false);
		}
	}

	/**
	 * Resolves a module-backed sub-API, yielding {@code null} when its module is disabled.
	 * <p>
	 * These getters are declared {@code @NotNull}, but the implementation cannot honour that: it
	 * resolves them through the module registry, so a disabled module yields {@code null} (or throws
	 * while resolving). Every lookup goes through here so one switched-off module can never break an
	 * enchant.
	 */
	@Nullable
	private static <T> T optional(Supplier<T> lookup) {
		try {
			return lookup.get();
		} catch (RuntimeException | LinkageError unavailable) {
			return null;
		}
	}

	/**
	 * Discards candidates that must not be broken and applies the block cap.
	 */
	private static List<Block> filterTargets(XPrisonEnchantsAPI enchants, AreaBreakContext context,
											 List<Block> candidates, Block origin, @Nullable AreaBounds region) {
		if (candidates == null || candidates.isEmpty()) {
			return Collections.emptyList();
		}
		final int cap = context.areaSettings().maxBlocks();
		final boolean providers = VirtualBlockProviders.hasAnyProviders();

		List<Block> targets = new ArrayList<>(candidates.size());
		for (Block block : candidates) {
			if (block == null || block.equals(origin)) {
				continue;
			}
			// With bounds this is arithmetic; without them we must ask per block.
			if (region != null ? !region.contains(block) : !enchants.isEnchantAllowed(block.getLocation())) {
				continue;
			}
			if (!isPresent(block, providers)) {
				continue;
			}
			targets.add(block);
			if (cap > 0 && targets.size() >= cap) {
				break;
			}
		}
		return targets;
	}

	/**
	 * A position holds something breakable when the world block is solid, or when it is air but a
	 * packet-mine provider still has a virtual block there.
	 */
	private static boolean isPresent(Block block, boolean providers) {
		if (!isAirBlock(block.getType())) {
			return true;
		}
		return providers && VirtualBlockProviders.hasProviderBlockAt(block.getLocation());
	}

	/**
	 * Air check by constant comparison — equivalent to {@link Material#isAir()} for block materials,
	 * without resolving the Bukkit block-type registry. Mirrors
	 * {@code VirtualBlockProviders}' identical check, and keeps the per-block hot path free of a
	 * registry lookup.
	 */
	private static boolean isAirBlock(Material material) {
		return material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR;
	}

	/**
	 * The normal, per-block path.
	 */
	private static void resolve(XPrisonAPI api, AreaBreakContext context, Player player,
								ItemStack pickaxe, Block origin, @Nullable AreaBounds region, List<Block> targets, boolean animations) {
		if (!player.isOnline()) {
			return;
		}
		final XPrisonEnchantsAPI enchants = optional(api::getEnchantsApi);
		if (enchants == null) {
			return;
		}

		final AreaBreakSettings settings = context.areaSettings();
		final boolean providers = VirtualBlockProviders.hasAnyProviders();

		// A PER_BLOCK enchant running on packet mines is auto-downgraded to AGGREGATE (unless the
		// owner opted out): per-block events buy nothing there and cost one dispatch per block.
		final boolean rewardOnly = enchants.isVirtualBreakEnabled(player, pickaxe);
		final BreakEventStrategy strategy = rewardOnly ? BreakEventStrategy.NONE
				: PacketMinePolicy.resolveStrategy(context.areaDisplayName(), settings.eventStrategy());

		// Re-validated here rather than trusting the selection: a deferred effect resolves long
		// afterwards, and the callback may legitimately supply a different set of blocks.
		List<Block> blocks = filterTargets(enchants, context, targets, origin, region);
		if (blocks.isEmpty()) {
			return;
		}

		// Above the chunking threshold the world-facing half of the proc is spread over several
		// ticks; the reward half still resolves here, in one pass. See AreaBreakChunking.
		if (AreaBreakChunking.shouldChunk(blocks.size())) {
			resolveChunked(api, context, enchants, player, pickaxe, origin, blocks,
					settings, strategy, rewardOnly, providers, animations);
			return;
		}

		// Virtual block types vanish the moment the provider removes them, yet pricing, drops and
		// downstream consumers read them afterwards; the snapshot keeps them resolvable. No-op when
		// no packet-mine plugin is installed.
		try (VirtualBlockProviders.SnapshotHandle ignored = VirtualBlockProviders.captureAndOpen(blocks)) {

			if (strategy == BreakEventStrategy.PER_BLOCK) {
				blocks = firePerBlockEvents(enchants, player, blocks);
				if (blocks.isEmpty()) {
					return;
				}
			}

			final XPrisonPickaxeLevelsAPI pickaxeLevels = optional(api::getPickaxeLevelsApi);

			// Must be summed while the blocks are still intact.
			final long expToAward = settings.countBlocksBroken() && pickaxeLevels != null
					? pickaxeLevels.getExpForBlocks(blocks) : 0L;

			// Lucky blocks set to be consumed pay out their rewards only; they are still cleared and
			// still count as broken, they just hand out no item and no sell value.
			final List<Block> rewardable = rewardableBlocks(api, blocks);

			BigDecimal earnings = BigDecimal.ZERO;
			if (!rewardable.isEmpty() && !routeToUltraBackpacks(api, player, rewardable, providers)) {
				earnings = collectDropsOrEarnings(api, enchants, player, pickaxe, rewardable);
			}

			if (!rewardOnly && context.shouldRemoveBlocks()) {
				clearBlocks(context, player, blocks, providers);
			}

			countTowardMines(api, context, rewardOnly, origin, blocks);

			if (settings.countBlocksBroken() && pickaxeLevels != null) {
				pickaxeLevels.addBlocksAndExp(player, pickaxe, blocks.size(), expToAward, PickaxeExpSource.AREA_ENCHANTS);
			}

			// With PER_BLOCK, X-Prison already ran this pipeline for each synthetic event; running it
			// again would double-count. With NONE the break is deliberately silent.
			if (strategy == BreakEventStrategy.AGGREGATE) {
				XPrisonBlocksAPI blocksApi = optional(api::getBlocksApi);
				if (blocksApi != null) {
					blocksApi.handleBlockBreak(player, blocks, settings.countBlocksBroken());
				}
			}

			payout(api, context, player, pickaxe, earnings);
		}

		if (animations) {
			context.onBreakComplete(player, blocks);
		}
	}

	/**
	 * The spread-out variant of {@link #resolve}, taken when a proc affects more blocks than
	 * {@link AreaBreakChunking#threshold()}.
	 * <p>
	 * Everything the player observes as a reward - drops or auto-sell earnings, the currency payout,
	 * the proc message, pickaxe blocks and experience - is resolved here, synchronously and exactly
	 * once, while the blocks are still intact and the player is still guaranteed to be online. Only
	 * the world-facing remainder (clearing the blocks, advancing the mine's reset counter and firing
	 * the aggregate break event) is handed to a {@link ChunkedAreaBreak}, which finishes it a slice
	 * per tick.
	 * <p>
	 * Paying before the first slice rather than after the last one is deliberate: no disconnect,
	 * death, world change, mine reset or server shutdown occurring part-way through the slices can
	 * then strand a payout the player has already earned, or duplicate one.
	 */
	private static void resolveChunked(XPrisonAPI api, AreaBreakContext context, XPrisonEnchantsAPI enchants,
									   Player player, ItemStack pickaxe, Block origin, List<Block> blocks,
									   AreaBreakSettings settings, BreakEventStrategy strategy,
									   boolean rewardOnly, boolean providers, boolean animations) {
		// Captured once for the whole proc and re-opened around each slice, so the virtual types stay
		// resolvable for every slice without ever leaving two overlays open at the same instant.
		final VirtualBlockProviders.Snapshot snapshot = VirtualBlockProviders.capture(blocks);

		List<Block> affected = blocks;
		try (VirtualBlockProviders.SnapshotHandle ignored = snapshot.open()) {

			if (strategy == BreakEventStrategy.PER_BLOCK) {
				affected = firePerBlockEvents(enchants, player, blocks);
				if (affected.isEmpty()) {
					return;
				}
			}

			final XPrisonPickaxeLevelsAPI pickaxeLevels = optional(api::getPickaxeLevelsApi);

			final long expToAward = settings.countBlocksBroken() && pickaxeLevels != null
					? pickaxeLevels.getExpForBlocks(affected) : 0L;

			final List<Block> rewardable = rewardableBlocks(api, affected);

			BigDecimal earnings = BigDecimal.ZERO;
			if (!rewardable.isEmpty() && !routeToUltraBackpacks(api, player, rewardable, providers)) {
				earnings = collectDropsOrEarnings(api, enchants, player, pickaxe, rewardable);
			}

			if (settings.countBlocksBroken() && pickaxeLevels != null) {
				pickaxeLevels.addBlocksAndExp(player, pickaxe, affected.size(), expToAward, PickaxeExpSource.AREA_ENCHANTS);
			}

			payout(api, context, player, pickaxe, earnings);
		}

		new ChunkedAreaBreak(api, context, player, origin, affected, snapshot,
				strategy == BreakEventStrategy.AGGREGATE, settings.countBlocksBroken(),
				rewardOnly, providers, animations).start();
	}

	/**
	 * One oversized area break in flight, finishing its world-facing work a slice per tick.
	 *
	 * <h2>Interleaving</h2>
	 * Every piece of per-proc state lives on this object, which is created per proc - never on the
	 * enchant, which is a single shared instance serving every player. Two players procing the same
	 * enchant therefore drive two independent instances whose slices may freely interleave.
	 *
	 * <h2>Exactly once</h2>
	 * The payout, the drops and the pickaxe award already happened before the first slice ran, so
	 * nothing here can duplicate them. Within the slices each block is visited exactly once (the
	 * cursor only ever moves forward) and the completion hook fires once, from {@link #finish()}.
	 *
	 * <h2>Giving up early</h2>
	 * Slicing stops when the mine has refilled underneath the proc - otherwise its tail would eat
	 * into a mine that has just reset - and when a packet-mine proc's owner has gone offline, since
	 * those blocks only ever existed in that one player's client. A proc against real world blocks
	 * keeps clearing even if its owner leaves: the mine is shared state and must not be left
	 * half-eaten.
	 */
	private static final class ChunkedAreaBreak {

		private final XPrisonAPI api;
		private final AreaBreakContext context;
		private final Player player;
		private final Block origin;
		private final List<Block> blocks;
		private final VirtualBlockProviders.Snapshot snapshot;
		private final boolean aggregate;
		private final boolean countBlocksBroken;
		private final boolean rewardOnly;
		private final boolean providers;
		private final boolean animations;
		private final boolean clearsBlocks;
		private final boolean virtual;
		private final int sliceSize;

		private int cursor;
		private int mineBlocksSeen = -1;
		private boolean finished;

		ChunkedAreaBreak(XPrisonAPI api, AreaBreakContext context, Player player, Block origin,
						 List<Block> blocks, VirtualBlockProviders.Snapshot snapshot,
						 boolean aggregate, boolean countBlocksBroken,
						 boolean rewardOnly, boolean providers, boolean animations) {
			this.api = api;
			this.context = context;
			this.player = player;
			this.origin = origin;
			this.blocks = blocks;
			this.snapshot = snapshot;
			this.aggregate = aggregate;
			this.countBlocksBroken = countBlocksBroken;
			this.rewardOnly = rewardOnly;
			this.providers = providers;
			this.animations = animations;
			this.clearsBlocks = !rewardOnly && context.shouldRemoveBlocks();
			this.virtual = providers && !snapshot.isEmpty();
			this.sliceSize = Math.max(1, AreaBreakChunking.threshold());
		}

		/** Starts the proc, running its first slice immediately so the break begins this tick. */
		void start() {
			this.mineBlocksSeen = currentMineBlocks();
			runSlice();
		}

		/**
		 * Runs one slice, then yields the tick. If the platform refuses to schedule the next slice
		 * the remaining ones are drained in this same loop rather than recursively, so work that has
		 * already been paid for is never dropped and the stack never grows with the block count.
		 */
		private void runSlice() {
			while (step()) {
				if (AreaBreakChunking.tryRunNextTick(this::runSlice)) {
					return;
				}
			}
			finish();
		}

		/**
		 * @return {@code true} if blocks remain to be processed after this slice
		 */
		private boolean step() {
			if (abandoned() || mineRefilled()) {
				return false;
			}

			final int end = Math.min(this.cursor + this.sliceSize, this.blocks.size());
			final List<Block> slice = this.blocks.subList(this.cursor, end);

			try (VirtualBlockProviders.SnapshotHandle ignored = this.snapshot.open()) {
				if (this.clearsBlocks) {
					clearBlocks(this.context, this.player, slice, this.providers);
				}

				countTowardMines(this.api, this.context, this.rewardOnly, this.origin, slice);

				if (this.aggregate) {
					XPrisonBlocksAPI blocksApi = optional(this.api::getBlocksApi);
					if (blocksApi != null) {
						blocksApi.handleBlockBreak(this.player, slice, this.countBlocksBroken);
					}
				}
			}

			this.cursor = end;
			this.mineBlocksSeen = currentMineBlocks();
			return this.cursor < this.blocks.size();
		}

		/** A packet-mine proc is meaningless once its owner has left; a real-world one is not. */
		private boolean abandoned() {
			return this.virtual && !this.player.isOnline();
		}

		/**
		 * Detects a mine reset that landed between two slices - the counter can only fall while this
		 * proc runs, so a rise (or an in-progress gradual reset) means the mine has been refilled and
		 * the remaining slices would eat into a mine that is standing again.
		 */
		private boolean mineRefilled() {
			if (this.mineBlocksSeen < 0) {
				return false;
			}
			Mine mine = mineAt(this.api, this.origin);
			return mine != null && (mine.isResetting() || mine.getCurrentBlocks() > this.mineBlocksSeen);
		}

		/**
		 * @return the mine's current block count, or {@code -1} when this proc does not account
		 * against a mine at all and refills need not be watched for
		 */
		private int currentMineBlocks() {
			if (this.rewardOnly || !this.context.shouldCountTowardMines()) {
				return -1;
			}
			Mine mine = mineAt(this.api, this.origin);
			return mine == null ? -1 : mine.getCurrentBlocks();
		}

		/** Fires the cosmetic completion hook exactly once, and only for a player still on the server. */
		private void finish() {
			if (this.finished) {
				return;
			}
			this.finished = true;
			if (this.animations && this.player.isOnline()) {
				this.context.onBreakComplete(this.player, this.blocks);
			}
		}
	}

	/**
	 * The bulk path: resolves an entire packet-mine region from the mine plugin's store without
	 * touching a single world block or firing a single per-block event.
	 */
	private static void resolveEntireVirtualRegion(XPrisonAPI api, AreaBreakContext context, Player player,
												   ItemStack pickaxe, AreaBounds region, boolean animations) {
		if (!player.isOnline()) {
			return;
		}

		final XPrisonEnchantsAPI enchants = optional(api::getEnchantsApi);
		final boolean rewardOnly = enchants != null && enchants.isVirtualBreakEnabled(player, pickaxe);

		Map<MineBlock, Long> broken = VirtualBlockProviders.collectRegion(player, region.world(),
				region.minX(), region.minY(), region.minZ(),
				region.maxX(), region.maxY(), region.maxZ(), !rewardOnly && context.shouldRemoveBlocks());
		if (broken.isEmpty()) {
			return;
		}

		final AreaBreakSettings settings = context.areaSettings();

		long totalBroken = 0L;
		for (long count : broken.values()) {
			totalBroken += count;
		}

		final XPrisonAutoSellAPI autoSellApi = optional(api::getAutoSellApi);
		final boolean autoSell = autoSellApi != null && autoSellApi.hasAutoSellEnabled(player);
		final int fortune = fortuneLevel(optional(api::getEnchantsApi), pickaxe);

		BigDecimal earnings = BigDecimal.ZERO;
		for (Map.Entry<MineBlock, Long> entry : broken.entrySet()) {
			if (autoSell) {
				// Priced once per type and multiplied by the count - the whole point of the bulk path.
				earnings = earnings.add(autoSellApi.getSellPriceForBlockExact(entry.getKey())
						.multiply(BigDecimal.valueOf(fortune))
						.multiply(BigDecimal.valueOf(entry.getValue())));
			} else {
				giveStacks(autoSellApi, player, entry.getKey(), entry.getValue() * fortune);
			}
		}

		if (settings.countBlocksBroken()) {
			XPrisonPickaxeLevelsAPI pickaxeLevels = optional(api::getPickaxeLevelsApi);
			if (pickaxeLevels != null) {
				pickaxeLevels.addBlocksAndExp(player, pickaxe,
						(int) Math.min(totalBroken, Integer.MAX_VALUE), totalBroken, PickaxeExpSource.AREA_ENCHANTS);
			}
			// Keeps quests, battle pass and boosters progressing without enumerating the blocks.
			XPrisonBlocksAPI blocksApi = optional(api::getBlocksApi);
			if (blocksApi != null) {
				blocksApi.handleBulkBlockBreak(player, broken);
			}
		}

		if (!rewardOnly && context.shouldCountTowardMines()) {
			XPrisonMinesAPI minesApi = optional(api::getMinesApi);
			if (minesApi != null) {
				Mine mine = minesApi.getMineAtLocation(
						new Location(region.world(), region.minX(), region.minY(), region.minZ()));
				if (mine != null) {
					mine.handleBlockBreak((int) Math.min(totalBroken, Integer.MAX_VALUE));
				}
			}
		}

		payout(api, context, player, pickaxe, earnings);
		if (animations) {
			context.onBreakComplete(player, Collections.emptyList());
		}
	}

	/**
	 * Fires one ignored {@link BlockBreakEvent} per block and keeps only those no other plugin
	 * cancelled, so per-block protection is honoured.
	 */
	private static List<Block> firePerBlockEvents(XPrisonEnchantsAPI enchants, Player player, List<Block> blocks) {
		List<Block> allowed = new ArrayList<>(blocks.size());
		for (Block block : blocks) {
			BlockBreakEvent event = new BlockBreakEvent(block, player);
			enchants.ignoreBlockBreakEvent(event);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				allowed.add(block);
			}
		}
		return allowed;
	}

	/**
	 * Drops the blocks whose item and sell value the Blocks module suppresses (lucky blocks configured
	 * with {@code give-block: false}). Returns the very same list when no such block is configured, so
	 * the overwhelmingly common setup pays nothing for this.
	 */
	private static List<Block> rewardableBlocks(XPrisonAPI api, List<Block> blocks) {
		final XPrisonBlocksAPI blocksApi = optional(api::getBlocksApi);
		if (blocksApi == null || !blocksApi.hasItemSuppressingLuckyBlocks()) {
			return blocks;
		}
		List<Block> rewardable = new ArrayList<>(blocks.size());
		for (Block block : blocks) {
			if (!blocksApi.isBlockItemSuppressed(block)) {
				rewardable.add(block);
			}
		}
		return rewardable;
	}

	private static boolean routeToUltraBackpacks(XPrisonAPI api, Player player, List<Block> blocks, boolean providers) {
		if (!api.isUltraBackpacksEnabled() || !UltraBackpacksSupport.isAvailable()) {
			return false;
		}
		// UltraBackpacks reads real world state, so a virtual block would resolve as air and be lost.
		if (providers && containsVirtual(blocks)) {
			return false;
		}
		return UltraBackpacksSupport.handleBlocksBroken(player, blocks);
	}

	private static boolean containsVirtual(List<Block> blocks) {
		for (Block block : blocks) {
			if (isAirBlock(block.getType()) && VirtualBlockProviders.hasBlockAt(block.getLocation())) {
				return true;
			}
		}
		return false;
	}

	private static BigDecimal collectDropsOrEarnings(XPrisonAPI api, XPrisonEnchantsAPI enchants,
													 Player player, ItemStack pickaxe, List<Block> blocks) {
		final XPrisonAutoSellAPI autoSellApi = optional(api::getAutoSellApi);
		final boolean autoSell = autoSellApi != null && autoSellApi.hasAutoSellEnabled(player);
		final int fortune = fortuneLevel(enchants, pickaxe);

		// On packet mines, pricing and drops are a pure function of the resolved block type, so we
		// resolve/price each distinct (type, amount) once and multiply by its count - collapsing the
		// per-block loop (and its BigDecimal / inventory.addItem churn) to O(distinct types), exactly
		// as resolveEntireVirtualRegion does. Gated behind the single optimize-packet-mining flag.
		if (PacketMinePolicy.isOptimizeForPacketMines() && VirtualBlockProviders.hasAnyProviders()) {
			return collectAggregated(api, enchants, autoSellApi, autoSell, player, blocks, fortune);
		}

		final MineBlockFactory factory = autoSell ? null : blockFactory(api);
		BigDecimal earnings = BigDecimal.ZERO;
		for (Block block : blocks) {
			int amount = enchants.isFortuneBlacklisted(block) ? 1 : fortune;
			if (autoSell) {
				earnings = earnings.add(autoSellApi.getPriceForBlockExact(block)
						.multiply(BigDecimal.valueOf(amount)));
			} else {
				giveDrop(factory, autoSellApi, player, block, amount);
			}
		}
		return earnings;
	}

	/**
	 * The packet-mine fast path for {@link #collectDropsOrEarnings}: buckets blocks by their resolved
	 * {@link MineBlock} type and per-block Fortune amount, then prices/drops once per bucket.
	 * <p>
	 * Behaviour-preserving relative to the per-block loop: sell pricing is type-keyed
	 * ({@code getSellPriceForBlockExact}), so {@code Σ price·amount} per block equals {@code price·amount·count}
	 * per bucket (BigDecimal sums are order-independent), and dropped item counts are identical. Blocks that
	 * cannot be resolved to a type are skipped, matching {@link #giveDrop}'s existing behaviour.
	 */
	private static BigDecimal collectAggregated(XPrisonAPI api, XPrisonEnchantsAPI enchants,
												XPrisonAutoSellAPI autoSellApi, boolean autoSell,
												Player player, List<Block> blocks, int fortune) {
		final MineBlockFactory factory = blockFactory(api);
		Map<AggregateKey, Long> counts = new HashMap<>();
		for (Block block : blocks) {
			MineBlock type;
			try {
				type = factory.fromBlock(block);
			} catch (IllegalArgumentException unresolvable) {
				continue;
			}
			int amount = enchants.isFortuneBlacklisted(block) ? 1 : fortune;
			counts.merge(new AggregateKey(type, amount), 1L, Long::sum);
		}

		if (!autoSell) {
			for (Map.Entry<AggregateKey, Long> bucket : counts.entrySet()) {
				giveStacks(autoSellApi, player, bucket.getKey().type(), bucket.getValue() * bucket.getKey().amount());
			}
			return BigDecimal.ZERO;
		}

		BigDecimal earnings = BigDecimal.ZERO;
		for (Map.Entry<AggregateKey, Long> bucket : counts.entrySet()) {
			earnings = earnings.add(autoSellApi.getSellPriceForBlockExact(bucket.getKey().type())
					.multiply(BigDecimal.valueOf(bucket.getKey().amount()))
					.multiply(BigDecimal.valueOf(bucket.getValue())));
		}
		return earnings;
	}

	/** Aggregation key for the packet-mine drop/earnings fast path: a block type paired with its Fortune amount. */
	private record AggregateKey(MineBlock type, int amount) {
	}

	private static int fortuneLevel(@Nullable XPrisonEnchantsAPI enchants, ItemStack pickaxe) {
		return enchants == null ? 1 : Math.max(1, enchants.getItemFortuneLevel(pickaxe));
	}

	/**
	 * The block factory is stateless, so it stays available even with the Blocks module disabled.
	 */
	private static MineBlockFactory blockFactory(XPrisonAPI api) {
		XPrisonBlocksAPI blocksApi = optional(api::getBlocksApi);
		return blocksApi != null ? blocksApi.getMineBlockFactory() : new MineBlockFactoryImpl();
	}

	/**
	 * Resolves through the mine-block factory so vanilla, custom (ItemsAdder/Nexo/Oraxen) and virtual
	 * blocks all yield the right item - {@code block.getType()} would be air for a virtual block.
	 */
	private static void giveDrop(MineBlockFactory factory, @Nullable XPrisonAutoSellAPI autoSellApi,
								 Player player, Block block, int amount) {
		try {
			ItemStack drop = factory.fromBlock(block).toItemStack(amount);
			if (drop != null && !drop.getType().isAir()) {
				player.getInventory().addItem(smelt(autoSellApi, drop));
			}
		} catch (IllegalArgumentException unresolvable) {
			// Nothing meaningful to give for this block.
		}
	}

	private static void giveStacks(@Nullable XPrisonAutoSellAPI autoSellApi, Player player, MineBlock type, long amount) {
		while (amount > 0) {
			int size = (int) Math.min(amount, 64L);
			ItemStack item = type.toItemStack(size);
			if (item == null || item.getType().isAir()) {
				return;
			}
			player.getInventory().addItem(smelt(autoSellApi, item));
			amount -= size;
		}
	}

	/**
	 * Routes a drop through AutoSell's auto-smelt mapping, so an area enchant hands out exactly what
	 * breaking the same block by hand would. A no-op when the AutoSell module is off.
	 */
	private static ItemStack smelt(@Nullable XPrisonAutoSellAPI autoSellApi, ItemStack drop) {
		if (autoSellApi == null) {
			return drop;
		}
		ItemStack smelted = autoSellApi.applyAutoSmelt(drop);
		return smelted == null || smelted.getType().isAir() ? drop : smelted;
	}

	/**
	 * Clears real blocks through the context's removal policy and removes virtual ones through the
	 * packet-mine provider in a single batched call rather than one call per block.
	 */
	private static void clearBlocks(AreaBreakContext context, Player player, List<Block> blocks, boolean providers) {
		List<Location> virtual = providers ? new ArrayList<>() : null;
		for (Block block : blocks) {
			if (virtual != null && isAirBlock(block.getType())) {
				virtual.add(block.getLocation());
			} else {
				context.removeRealBlock(player, block);
			}
		}
		if (virtual != null && !virtual.isEmpty()) {
			VirtualBlockProviders.breakBlocks(player, virtual);
		}
	}

	/**
	 * Resolves the mine the origin block sits in, or {@code null} when the mines module is off or the
	 * break happened outside every mine.
	 */
	@Nullable
	private static Mine mineAt(XPrisonAPI api, Block origin) {
		XPrisonMinesAPI minesApi = optional(api::getMinesApi);
		return minesApi == null ? null : minesApi.getMineAtLocation(origin.getLocation());
	}

	private static void countTowardMines(XPrisonAPI api, AreaBreakContext context, boolean rewardOnly, Block origin, List<Block> blocks) {
		if (rewardOnly || !context.shouldCountTowardMines()) {
			return;
		}
		Mine mine = mineAt(api, origin);
		if (mine != null) {
			boolean setToAir = context.minesClearBlocks();
			// On packet mines, when the pipeline already ran clearBlocks (shouldRemoveBlocks), the blocks
			// are already gone (virtual removed via the batched provider call, real set to AIR), so the
			// mines module's per-block setType(AIR) is pure waste. currentBlocks is decremented regardless,
			// so reset accounting is unaffected. Guarded by shouldRemoveBlocks so reward-only enchants that
			// rely on the mines module as their only clearer are untouched.
			if (setToAir && context.shouldRemoveBlocks()
					&& PacketMinePolicy.isOptimizeForPacketMines()
					&& VirtualBlockProviders.hasAnyProviders()) {
				setToAir = false;
			}
			// Copied: the mines module prunes entries outside its own region from the list it is given.
			mine.handleBlockBreak(new ArrayList<>(blocks), setToAir);
		}
	}

	/**
	 * Applies the prestige multiplier, credits the currency and reports what was actually paid.
	 */
	private static void payout(XPrisonAPI api, AreaBreakContext context, Player player, ItemStack pickaxe, BigDecimal earnings) {
		if (earnings == null || earnings.signum() <= 0) {
			return;
		}
		XPrisonCurrencyAPI currencyApi = optional(api::getCurrencyApi);
		if (currencyApi == null) {
			return; // currency module disabled - nothing to pay into
		}

		final AreaBreakSettings settings = context.areaSettings();
		XPrisonCurrency currency = currencyApi.getCurrency(settings.currencyToGive());
		if (currency == null) {
			Bukkit.getLogger().warning("[X-Prison] Area enchant '" + context.areaDisplayName()
					+ "' is configured with unknown currency '" + settings.currencyToGive()
					+ "'; its earnings were not paid.");
			return;
		}

		BigDecimal amount = context.applyAreaPrestige(earnings, pickaxe);
		BigDecimal credited = currencyApi.addBalance(player, currency, amount, ReceiveCause.MINING);

		String message = settings.message();
		if (message != null && !message.isEmpty() && credited.signum() > 0 && context.shouldSendProcMessage(player, pickaxe)) {
			// Reports the credited amount, which a currency balance cap may have clamped.
			api.getTextApi().sendMessage(player, message
					.replace("%amount%", currency.format(credited))
					.replace("%currency%", currency.getDisplayName()));
		}
	}
}
