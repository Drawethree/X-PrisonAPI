package dev.drawethree.xprison.api.enchants.area;

import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProviders;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Guards the one area-enchant misconfiguration that silently wrecks performance: firing a Bukkit
 * {@link org.bukkit.event.block.BlockBreakEvent} <b>per block</b> ({@link BreakEventStrategy#PER_BLOCK},
 * i.e. legacy {@code useEvents: true}) while a packet-mines provider is active.
 * <p>
 * Packet mines learn about a break through the batched
 * {@link VirtualBlockProviders#breakBlocks} call, so per-block events buy nothing there while costing
 * one full synchronous event dispatch per broken block — the exact O(area) main-thread stall that
 * shows up to players as TPS loss and ping spikes. Worse, those events describe blocks that are
 * server-side air, which other plugins can act on incorrectly.
 *
 * <h2>Behaviour</h2>
 * When {@link #isOptimizeForPacketMines() optimisation} is enabled (the default), a {@code PER_BLOCK}
 * enchant running on a server with a registered provider is transparently downgraded to
 * {@link BreakEventStrategy#AGGREGATE} — X-Prison's own consumers (quests, battle pass, boosters,
 * statistics, mine reset via the batched break) keep working. When it is disabled, the configured
 * strategy is honoured unchanged. Either way the situation is logged <b>once per enchant</b> so the
 * server owner sees why, without spamming the console on the mining hot path.
 * <p>
 * The decision is made at proc time rather than at load time on purpose: a packet-mines plugin may
 * enable after X-Prison, so only the running server knows whether a provider is actually present.
 *
 * @since 1.9
 */
public final class PacketMinePolicy {

	private static final Logger LOGGER = Logger.getLogger("Minecraft");

	private static volatile boolean optimizeForPacketMines = true;

	private static final Set<String> loggedEnchants = ConcurrentHashMap.newKeySet();

	private PacketMinePolicy() {
	}

	/**
	 * Enables or disables the automatic {@code PER_BLOCK} → {@code AGGREGATE} downgrade for packet
	 * mines. Changing the value clears the once-per-enchant log guard so the new behaviour is
	 * re-announced on the next relevant proc.
	 *
	 * @param enabled {@code true} to auto-optimise (default), {@code false} to honour the configured
	 *                strategy unchanged
	 */
	public static void setOptimizeForPacketMines(boolean enabled) {
		optimizeForPacketMines = enabled;
		loggedEnchants.clear();
	}

	/**
	 * @return whether the automatic packet-mine optimisation is enabled
	 */
	public static boolean isOptimizeForPacketMines() {
		return optimizeForPacketMines;
	}

	/**
	 * Resolves the strategy an area enchant should actually run with, given its configured strategy
	 * and the live server state, warning once per enchant when the packet-mine footgun is detected.
	 *
	 * @param enchantName the enchant's display name, used for the once-per-enchant log guard
	 * @param configured  the strategy read from the enchant's configuration
	 * @return {@link BreakEventStrategy#AGGREGATE} when a {@code PER_BLOCK} enchant is auto-optimised
	 * for active packet mines; otherwise {@code configured} unchanged
	 */
	@NotNull
	public static BreakEventStrategy resolveStrategy(@NotNull String enchantName, @NotNull BreakEventStrategy configured) {
		if (configured != BreakEventStrategy.PER_BLOCK || !VirtualBlockProviders.hasAnyProviders()) {
			return configured;
		}

		if (optimizeForPacketMines) {
			if (loggedEnchants.add(enchantName)) {
				LOGGER.warning("[X-Prison] Area enchant '" + enchantName
						+ "' uses per-block break events (useEvents: true) while packet mines are active."
						+ " Auto-optimising it to the aggregate event to protect TPS — packet mines are already"
						+ " notified of the break directly. Set its config to useEvents: false to silence this,"
						+ " or disable 'optimize-packet-mining' in enchants.yml to force per-block events.");
			}
			return BreakEventStrategy.AGGREGATE;
		}

		if (loggedEnchants.add(enchantName)) {
			LOGGER.warning("[X-Prison] Area enchant '" + enchantName
					+ "' uses per-block break events (useEvents: true) while packet mines are active."
					+ " This fires one Bukkit BlockBreakEvent per broken block on the main thread and can"
					+ " cause severe TPS/ping loss. 'optimize-packet-mining' is disabled, so the configured"
					+ " behaviour is being kept — set useEvents: false, or re-enable optimisation to auto-fix.");
		}
		return configured;
	}
}
