package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import dev.drawethree.xprison.api.utils.BigNumbers;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

/**
 * Event called when a player enchants a pickaxe.
 * <p>
 * This event contains details about the player, the token cost of the enchantment,
 * and the level of the enchant applied.
 * <p>
 * The event is cancellable, allowing listeners to prevent the enchantment from proceeding.
 */
@Getter
public final class XPrisonPlayerEnchantEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The token cost required for this enchantment, clamped to {@code long} range.
	 * Kept in sync with {@link #tokenCostExact}; prefer {@link #getTokenCostExact()}.
	 */
	private long tokenCost;

	/**
	 * The exact token cost required for this enchantment. Never clamps, so high-level exponential
	 * costs stay exact instead of overflowing negative.
	 */
	private BigDecimal tokenCostExact;

	/**
	 * The level of the enchantment being applied.
	 */
	private final int level;

	/**
	 * Whether this event has been cancelled.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new {@code XPrisonPlayerEnchantEvent}.
	 *
	 * @param player    The player enchanting the pickaxe.
	 * @param tokenCost The exact cost of the enchantment in tokens.
	 * @param level     The level of the enchantment.
	 */
	public XPrisonPlayerEnchantEvent(Player player, BigDecimal tokenCost, int level) {
		super(player);
		this.tokenCostExact = tokenCost;
		this.tokenCost = BigNumbers.clampToLong(tokenCost);
		this.level = level;
	}

	/**
	 * Constructs a new {@code XPrisonPlayerEnchantEvent}.
	 *
	 * @param player    The player enchanting the pickaxe.
	 * @param tokenCost The cost of the enchantment in tokens.
	 * @param level     The level of the enchantment.
	 * @deprecated a {@code long} cost overflows for high-level exponential curves;
	 *             use {@link #XPrisonPlayerEnchantEvent(Player, BigDecimal, int)}.
	 */
	@Deprecated
	public XPrisonPlayerEnchantEvent(Player player, long tokenCost, int level) {
		this(player, BigDecimal.valueOf(tokenCost), level);
	}

	/**
	 * Sets the exact token cost, keeping the saturating {@code long} view in sync.
	 *
	 * @param tokenCost the exact cost of the enchantment
	 */
	public void setTokenCostExact(BigDecimal tokenCost) {
		this.tokenCostExact = tokenCost;
		this.tokenCost = BigNumbers.clampToLong(tokenCost);
	}

	/**
	 * @param tokenCost the cost of the enchantment in tokens
	 * @deprecated a {@code long} cost saturates for high-level curves; use {@link #setTokenCostExact(BigDecimal)}.
	 */
	@Deprecated
	public void setTokenCost(long tokenCost) {
		setTokenCostExact(BigDecimal.valueOf(tokenCost));
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return The static handler list.
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return The static handler list.
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Checks whether this event is cancelled.
	 *
	 * @return {@code true} if the event is cancelled, {@code false} otherwise.
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Sets the cancellation state of this event.
	 *
	 * @param cancel {@code true} to cancel the event, {@code false} to allow it to proceed.
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
