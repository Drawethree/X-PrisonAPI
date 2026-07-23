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

		if (region != null && context.breaksEntireRegion() && VirtualBlockProviders.isVirtualMineArea(originLocation)) {
			context.onBreakStart(player, origin, level);
			context.dispatchWithEffect(player, origin, Collections.emptyList(), level,
					ignoredTargets -> resolveEntireVirtualRegion(api, context, player, pickaxe, region));
			return;
		}

		List<Block> targets = filterTargets(enchants, context, context.selectTargets(player, origin, region, level), origin, region);
		if (targets.isEmpty()) {
			return;
		}

		context.onBreakStart(player, origin, level);
		context.dispatchWithEffect(player, origin, targets, level,
				finalTargets -> resolve(api, context, player, pickaxe, origin, region, finalTargets));
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
		if (!block.getType().isAir()) {
			return true;
		}
		return providers && VirtualBlockProviders.hasProviderBlockAt(block.getLocation());
	}

	/**
	 * The normal, per-block path.
	 */
	private static void resolve(XPrisonAPI api, AreaBreakContext context, Player player,
								ItemStack pickaxe, Block origin, @Nullable AreaBounds region, List<Block> targets) {
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
		final BreakEventStrategy strategy = PacketMinePolicy.resolveStrategy(context.areaDisplayName(), settings.eventStrategy());

		// Re-validated here rather than trusting the selection: a deferred effect resolves long
		// afterwards, and the callback may legitimately supply a different set of blocks.
		List<Block> blocks = filterTargets(enchants, context, targets, origin, region);
		if (blocks.isEmpty()) {
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

			BigDecimal earnings = BigDecimal.ZERO;
			if (!routeToUltraBackpacks(api, player, blocks, providers)) {
				earnings = collectDropsOrEarnings(api, enchants, player, pickaxe, blocks);
			}

			if (context.shouldRemoveBlocks()) {
				clearBlocks(context, player, blocks, providers);
			}

			countTowardMines(api, context, origin, blocks);

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

		context.onBreakComplete(player, blocks);
	}

	/**
	 * The bulk path: resolves an entire packet-mine region from the mine plugin's store without
	 * touching a single world block or firing a single per-block event.
	 */
	private static void resolveEntireVirtualRegion(XPrisonAPI api, AreaBreakContext context, Player player,
												   ItemStack pickaxe, AreaBounds region) {
		if (!player.isOnline()) {
			return;
		}

		Map<MineBlock, Long> broken = VirtualBlockProviders.collectRegion(player, region.world(),
				region.minX(), region.minY(), region.minZ(),
				region.maxX(), region.maxY(), region.maxZ(), context.shouldRemoveBlocks());
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
				giveStacks(player, entry.getKey(), entry.getValue() * fortune);
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

		if (context.shouldCountTowardMines()) {
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
		context.onBreakComplete(player, Collections.emptyList());
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
			if (block.getType().isAir() && VirtualBlockProviders.hasBlockAt(block.getLocation())) {
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
				giveDrop(factory, player, block, amount);
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
				giveStacks(player, bucket.getKey().type(), bucket.getValue() * bucket.getKey().amount());
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
	private static void giveDrop(MineBlockFactory factory, Player player, Block block, int amount) {
		try {
			ItemStack drop = factory.fromBlock(block).toItemStack(amount);
			if (drop != null && !drop.getType().isAir()) {
				player.getInventory().addItem(drop);
			}
		} catch (IllegalArgumentException unresolvable) {
			// Nothing meaningful to give for this block.
		}
	}

	private static void giveStacks(Player player, MineBlock type, long amount) {
		while (amount > 0) {
			int size = (int) Math.min(amount, 64L);
			ItemStack item = type.toItemStack(size);
			if (item == null || item.getType().isAir()) {
				return;
			}
			player.getInventory().addItem(item);
			amount -= size;
		}
	}

	/**
	 * Clears real blocks through the context's removal policy and removes virtual ones through the
	 * packet-mine provider in a single batched call rather than one call per block.
	 */
	private static void clearBlocks(AreaBreakContext context, Player player, List<Block> blocks, boolean providers) {
		List<Location> virtual = providers ? new ArrayList<>() : null;
		for (Block block : blocks) {
			if (virtual != null && block.getType().isAir()) {
				virtual.add(block.getLocation());
			} else {
				context.removeRealBlock(player, block);
			}
		}
		if (virtual != null && !virtual.isEmpty()) {
			VirtualBlockProviders.breakBlocks(player, virtual);
		}
	}

	private static void countTowardMines(XPrisonAPI api, AreaBreakContext context, Block origin, List<Block> blocks) {
		if (!context.shouldCountTowardMines()) {
			return;
		}
		XPrisonMinesAPI minesApi = optional(api::getMinesApi);
		if (minesApi == null) {
			return; // mines module disabled - the server has no mine to account against
		}
		Mine mine = minesApi.getMineAtLocation(origin.getLocation());
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
		if (message != null && !message.isEmpty() && credited.signum() > 0) {
			// Reports the credited amount, which a currency balance cap may have clamped.
			api.getTextApi().sendMessage(player, message
					.replace("%amount%", currency.format(credited))
					.replace("%currency%", currency.getDisplayName()));
		}
	}
}
