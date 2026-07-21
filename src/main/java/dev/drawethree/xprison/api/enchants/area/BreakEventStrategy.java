package dev.drawethree.xprison.api.enchants.area;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * How an {@link AreaBreakEnchant} announces the blocks it destroys.
 * <p>
 * This is the single most important performance/compatibility trade-off an area enchant makes.
 * Broadcasting each block individually is what lets <i>any</i> third-party plugin (mine-reset
 * counters, protection, job/quest plugins) observe the break — but it costs one full event
 * dispatch per block. Aggregating is dramatically cheaper but is only understood by plugins that
 * know X-Prison's own event.
 *
 * @since 1.9
 */
public enum BreakEventStrategy {

	/**
	 * Fire one Bukkit {@link org.bukkit.event.block.BlockBreakEvent} per affected block.
	 * <p>
	 * This is the <b>default and the most compatible</b> mode, and the only one external plugins
	 * that do not know X-Prison can observe. Each synthetic event is registered through
	 * {@link dev.drawethree.xprison.api.enchants.XPrisonEnchantsAPI#ignoreBlockBreakEvent} so
	 * X-Prison's own gated listeners do not double-process it, while external listeners still
	 * receive it in full and may <b>cancel</b> individual blocks (per-block protection is honoured —
	 * cancelled blocks are dropped from the affected set).
	 * <p>
	 * Cost: O(affected blocks) event dispatches per proc.
	 */
	PER_BLOCK,

	/**
	 * Fire no per-block events; announce the whole break once via X-Prison's aggregate
	 * {@link dev.drawethree.xprison.api.shared.events.XPrisonBlockBreakEvent}.
	 * <p>
	 * Much cheaper than {@link #PER_BLOCK} and still drives X-Prison's own consumers (quests,
	 * battle pass, block boosters, blocks statistics, lucky blocks). External plugins that only
	 * listen to the Bukkit event will <b>not</b> see these breaks, and per-block protection cannot
	 * veto individual blocks.
	 * <p>
	 * Cost: O(1) event dispatches per proc.
	 */
	AGGREGATE,

	/**
	 * Fire nothing at all. The enchant still sells/gives drops, clears the blocks and pays the
	 * player, but no break is announced to anyone.
	 * <p>
	 * The fastest mode; use only when the enchant deliberately must not interact with any other
	 * system.
	 */
	NONE;

	/**
	 * Maps the legacy boolean {@code useEvents} JSON flag onto a strategy, preserving the historical
	 * meaning: {@code true} fired per-block Bukkit events, {@code false} fired the aggregate event.
	 *
	 * @param useEvents the legacy flag value
	 * @return {@link #PER_BLOCK} when {@code true}, otherwise {@link #AGGREGATE}
	 */
	@NotNull
	public static BreakEventStrategy fromLegacyUseEvents(boolean useEvents) {
		return useEvents ? PER_BLOCK : AGGREGATE;
	}

	/**
	 * Parses a strategy from its configuration name, case-insensitively.
	 *
	 * @param name     the configured value (e.g. {@code "per_block"}); may be {@code null}
	 * @param fallback returned when {@code name} is {@code null}, blank or unrecognised
	 * @return the parsed strategy, or {@code fallback}
	 */
	@NotNull
	public static BreakEventStrategy parse(@Nullable String name, @NotNull BreakEventStrategy fallback) {
		if (name == null || name.isBlank()) {
			return fallback;
		}
		try {
			return valueOf(name.trim().toUpperCase(java.util.Locale.ROOT));
		} catch (IllegalArgumentException unknown) {
			return fallback;
		}
	}
}
