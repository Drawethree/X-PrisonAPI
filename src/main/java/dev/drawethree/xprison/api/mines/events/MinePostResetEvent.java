package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired after a mine has been reset.
 * <p>
 * This event is called once the mine reset process has completed.
 */
@Getter
public final class MinePostResetEvent extends XPrisonEvent {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final Mine mine;

	/**
	 * Constructs a new MinePostResetEvent.
	 *
	 * @param mine the {@link Mine} that has been reset
	 */
	public MinePostResetEvent(Mine mine) {
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
