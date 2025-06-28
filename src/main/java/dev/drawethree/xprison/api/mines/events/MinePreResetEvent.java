package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired before a mine is reset.
 * <p>
 * This event is called when a mine reset is about to occur.
 * Listeners can cancel this event to prevent the reset.
 */
@Getter
public final class MinePreResetEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final Mine mine;

	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new MinePreResetEvent.
	 *
	 * @param mine the {@link Mine} that is about to be reset
	 */
	public MinePreResetEvent(Mine mine) {
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
