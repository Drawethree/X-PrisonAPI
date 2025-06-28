package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a gang is disbanded.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the gang from being disbanded.
 */
public final class GangDisbandEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	/**
	 * The gang that is being disbanded.
	 */
	@Getter
	private final Gang gang;

	private boolean cancelled;

	/**
	 * Constructs a new {@link GangDisbandEvent}.
	 *
	 * @param gang the {@link Gang} that is being disbanded
	 */
	public GangDisbandEvent(Gang gang) {
		this.gang = gang;
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
	 * Gets the list of handlers for this event instance.
	 *
	 * @return the {@link HandlerList}
	 */
	@Override
	public HandlerList getHandlers() {
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
}
