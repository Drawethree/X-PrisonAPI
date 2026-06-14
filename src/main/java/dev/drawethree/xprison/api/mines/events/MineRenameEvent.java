package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a mine is about to be renamed.
 * <p>
 * This event is cancellable; cancelling it leaves the mine's name unchanged.
 */
@Getter
public final class MineRenameEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	@Setter
	private boolean cancelled;

	private final Mine mine;

	private final String oldName;

	private final String newName;

	/**
	 * Constructs a new MineRenameEvent.
	 *
	 * @param mine    the {@link Mine} being renamed
	 * @param oldName the mine's current name
	 * @param newName the proposed new name
	 */
	public MineRenameEvent(Mine mine, String oldName, String newName) {
		this.mine = mine;
		this.oldName = oldName;
		this.newName = newName;
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
