package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a mine is created.
 * <p>
 * This event is cancellable; cancelling it will prevent the mine creation.
 */
@Getter
public final class MineCreateEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	@Setter
	private boolean cancelled;

	private final CommandSender creator;

	private final Mine mine;

	/**
	 * Constructs a new MineCreateEvent.
	 *
	 * @param creator the {@link CommandSender} who created the mine
	 * @param mine    the {@link Mine} that was created
	 */
	public MineCreateEvent(Player creator, Mine mine) {
		this.creator = creator;
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
