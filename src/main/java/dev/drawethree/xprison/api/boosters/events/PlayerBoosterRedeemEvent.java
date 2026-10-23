package dev.drawethree.xprison.api.boosters.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player right-clicks a booster item, before the booster is queued and the item is
 * taken.
 * <p>
 * This event is {@link Cancellable}; cancelling it keeps the item and queues nothing.
 *
 * @since 1.10
 */
@Getter
public final class PlayerBoosterRedeemEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	/**
	 * The id of the booster definition in {@code boosters.yml}.
	 */
	private final String boosterId;

	/**
	 * The booster's target: a currency name or {@code "battlepass-xp"}.
	 */
	private final String target;

	/**
	 * The booster's multiplier.
	 */
	private final double multiplier;

	/**
	 * The booster's duration in seconds.
	 */
	private final long durationSeconds;

	/**
	 * Constructs a new {@link PlayerBoosterRedeemEvent}.
	 *
	 * @param player          the player redeeming the item
	 * @param boosterId       the booster definition id
	 * @param target          the booster's target
	 * @param multiplier      the booster's multiplier
	 * @param durationSeconds the booster's duration in seconds
	 */
	public PlayerBoosterRedeemEvent(Player player, String boosterId, String target, double multiplier, long durationSeconds) {
		super(player);
		this.boosterId = boosterId;
		this.target = target;
		this.multiplier = multiplier;
		this.durationSeconds = durationSeconds;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
