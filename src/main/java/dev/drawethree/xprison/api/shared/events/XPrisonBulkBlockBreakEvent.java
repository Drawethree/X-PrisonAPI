package dev.drawethree.xprison.api.shared.events;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fired when many blocks are destroyed at once and enumerating them individually would be
 * prohibitively expensive — the counterpart to {@link XPrisonBlockBreakEvent} for bulk breaks.
 * <p>
 * The motivating case is a packet-based ("virtual") mine: those blocks exist only in the mine
 * plugin's in-memory store and client-side, so there are no real {@link org.bukkit.block.Block}
 * handles to hand out, and materialising a list of hundreds of thousands of them would defeat the
 * purpose of the bulk path. Instead this event carries <b>how many of each block type</b> were
 * broken, which is O(distinct types) regardless of the volume destroyed.
 * <p>
 * Consumers that only need a count (quests' "mine N blocks", battle pass mining XP) should read
 * {@link #getTotalBlocks()}; consumers that need per-type detail (type-specific quests) should read
 * {@link #getTypeCounts()}. Multiplier-style listeners (e.g. a block booster) may replace the map
 * via {@link #setTypeCounts(Map)} to scale the recorded amounts.
 *
 * @see XPrisonBlockBreakEvent
 * @since 1.9
 */
public final class XPrisonBulkBlockBreakEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;

	private Map<MineBlock, Long> typeCounts;

	private boolean cancelled;

	/**
	 * Constructs a new bulk block-break event.
	 *
	 * @param player     the player who caused the break
	 * @param typeCounts how many blocks of each type were broken; copied defensively
	 */
	public XPrisonBulkBlockBreakEvent(@NotNull Player player, @NotNull Map<MineBlock, Long> typeCounts) {
		super(player);
		this.player = player;
		this.typeCounts = new LinkedHashMap<>(typeCounts);
	}

	/**
	 * @return the player who caused the break
	 */
	@NotNull
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * @return an unmodifiable view of how many blocks of each type were broken
	 */
	@NotNull
	public Map<MineBlock, Long> getTypeCounts() {
		return Collections.unmodifiableMap(this.typeCounts);
	}

	/**
	 * Replaces the recorded per-type counts, for listeners that scale the break (e.g. a block
	 * booster doubling the amounts). The map is copied defensively.
	 *
	 * @param typeCounts the new per-type counts
	 */
	public void setTypeCounts(@NotNull Map<MineBlock, Long> typeCounts) {
		this.typeCounts = new LinkedHashMap<>(typeCounts);
	}

	/**
	 * @return the total number of blocks broken, i.e. the sum of {@link #getTypeCounts()} values
	 */
	public long getTotalBlocks() {
		long total = 0L;
		for (long count : this.typeCounts.values()) {
			total += count;
		}
		return total;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Returns the static handler list for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
