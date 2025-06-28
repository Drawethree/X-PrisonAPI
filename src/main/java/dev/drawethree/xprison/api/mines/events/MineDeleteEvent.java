package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a mine is about to be deleted.
 * <p>
 * This event is cancellable; cancelling it will prevent the mine from being deleted.
 */
@Getter
public final class MineDeleteEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	@Setter
	private boolean cancelled;

	private final Mine mine;

	/**
	 * Constructs a new MineDeleteEvent.
	 *
	 * @param mine the {@link Mine} that is being deleted
	 */
	public MineDeleteEvent(Mine mine) {
		this.mine = mine;
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	/**
	 * Gets the handlers for this event.
	 *
	 * @return the handler list
	 */
	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
