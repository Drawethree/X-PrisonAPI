package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.gangs.enums.GangLeaveReason;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event that is fired when a player leaves a gang.
 * This can be due to a voluntary departure, kick, disband, or other {@link GangLeaveReason}s.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the player from leaving.
 */
public final class GangLeaveEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The player who is leaving the gang.
	 */
	@Getter
	private final OfflinePlayer player;

	/**
	 * The gang that the player is leaving.
	 */
	@Getter
	private final Gang gang;

	/**
	 * The reason the player is leaving the gang.
	 */
	@Getter
	private final GangLeaveReason leaveReason;

	/**
	 * Constructs a new {@link GangLeaveEvent}.
	 *
	 * @param player      the {@link OfflinePlayer} who is leaving the gang
	 * @param gang        the {@link Gang} being left
	 * @param leaveReason the {@link GangLeaveReason} explaining why the player is leaving
	 */
	public GangLeaveEvent(OfflinePlayer player, Gang gang, GangLeaveReason leaveReason) {
		super(player);
		this.player = player;
		this.gang = gang;
		this.leaveReason = leaveReason;
	}

	/**
	 * Gets the static handler list for this event.
	 *
	 * @return the {@link HandlerList}
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	/**
	 * Checks whether the event has been cancelled.
	 *
	 * @return {@code true} if cancelled, {@code false} otherwise
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Sets whether the event should be cancelled.
	 *
	 * @param cancel {@code true} to cancel the event
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	/**
	 * Gets the list of handlers for this event instance.
	 *
	 * @return the {@link HandlerList}
	 */
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
