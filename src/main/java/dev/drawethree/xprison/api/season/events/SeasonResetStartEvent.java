package dev.drawethree.xprison.api.season.events;

import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Fired on the main thread right before a season reset starts wiping data.
 * <p>
 * This event is {@link Cancellable}; cancelling it stops the reset before anything is deleted.
 *
 * @since 1.10
 */
public final class SeasonResetStartEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final List<String> participantIds;
	private boolean cancelled;

	/**
	 * Constructs a new {@link SeasonResetStartEvent}.
	 *
	 * @param participantIds the ids of the participants about to be reset, in order
	 */
	public SeasonResetStartEvent(List<String> participantIds) {
		this.participantIds = List.copyOf(participantIds);
	}

	/**
	 * Gets the participants about to be reset, in the order they will run.
	 *
	 * @return the participant ids
	 */
	public List<String> getParticipantIds() {
		return this.participantIds;
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
