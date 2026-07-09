package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a gang's ownership is transferred to another member.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the transfer.
 */
public final class GangOwnershipTransferEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The gang whose ownership is being transferred.
	 */
	@Getter
	private final Gang gang;

	/**
	 * The previous owner of the gang.
	 */
	@Getter
	private final OfflinePlayer oldOwner;

	/**
	 * The new owner of the gang.
	 */
	@Getter
	private final OfflinePlayer newOwner;

	/**
	 * Constructs a new {@link GangOwnershipTransferEvent}.
	 *
	 * @param gang     the {@link Gang} whose ownership is being transferred
	 * @param oldOwner the previous owner
	 * @param newOwner the new owner
	 */
	public GangOwnershipTransferEvent(Gang gang, OfflinePlayer oldOwner, OfflinePlayer newOwner) {
		this.gang = gang;
		this.oldOwner = oldOwner;
		this.newOwner = newOwner;
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
