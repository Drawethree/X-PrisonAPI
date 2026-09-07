package dev.drawethree.xprison.api.milestones.events;

import dev.drawethree.xprison.api.milestones.model.Milestone;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

/**
 * Event fired when a player is about to be paid out for reaching a milestone.
 * <p>
 * This event is {@link Cancellable}; cancelling it suppresses the whole payout - title, message,
 * broadcast, sound, firework and reward commands. The player's progress is still recorded, so a
 * cancelled milestone does not fire again the next time the statistic moves.
 */
@Getter
public final class PlayerMilestoneReachedEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	/**
	 * The milestone that was reached.
	 */
	private final Milestone milestone;

	/**
	 * The value of the tracked statistic that reached the milestone. This is at least the
	 * milestone's threshold and may be higher when a single jump crossed it.
	 */
	private final BigDecimal value;

	/**
	 * Constructs a new {@link PlayerMilestoneReachedEvent}.
	 *
	 * @param player    the player who reached the milestone
	 * @param milestone the milestone that was reached
	 * @param value     the value of the tracked statistic that reached it
	 */
	public PlayerMilestoneReachedEvent(Player player, Milestone milestone, BigDecimal value) {
		super(player);
		this.milestone = milestone;
		this.value = value;
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
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
