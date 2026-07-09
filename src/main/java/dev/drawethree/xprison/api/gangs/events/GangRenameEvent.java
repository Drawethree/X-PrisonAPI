package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a gang is renamed.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the rename.
 * The resulting {@code newName} may be adjusted by listeners via {@link #setNewName(String)}.
 */
public final class GangRenameEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The gang being renamed.
	 */
	@Getter
	private final Gang gang;

	/**
	 * The gang's name before the rename.
	 */
	@Getter
	private final String oldName;

	/**
	 * The gang's name after the rename. Mutable so listeners can adjust the final name.
	 */
	@Getter
	@Setter
	private String newName;

	/**
	 * The sender who initiated the rename (may be the console for admin renames).
	 */
	@Getter
	private final CommandSender whoRenamed;

	/**
	 * Constructs a new {@link GangRenameEvent}.
	 *
	 * @param gang       the {@link Gang} being renamed
	 * @param oldName    the name before the rename
	 * @param newName    the name after the rename
	 * @param whoRenamed the {@link CommandSender} who initiated the rename
	 */
	public GangRenameEvent(Gang gang, String oldName, String newName, CommandSender whoRenamed) {
		this.gang = gang;
		this.oldName = oldName;
		this.newName = newName;
		this.whoRenamed = whoRenamed;
	}

	/**
	 * Gets the static handler list for this event.
	 *
	 * @return the {@link HandlerList}
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
