package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a gang is created.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the gang creation.
 */
public final class GangCreateEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The leader of the gang.
	 */
	@Getter
	private final OfflinePlayer gangLeader;

	/**
	 * The gang that has been created.
	 */
	@Getter
	private final Gang gang;

	/**
	 * Constructs a new {@link GangCreateEvent}.
	 *
	 * @param gangLeader the {@link OfflinePlayer} who will be the gang leader
	 * @param gang       the created {@link Gang}
	 */
	public GangCreateEvent(OfflinePlayer gangLeader, Gang gang) {
		this.gangLeader = gangLeader;
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
