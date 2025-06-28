package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event that is fired when a player joins a gang.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the player from joining.
 */
public final class GangJoinEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The player who is joining the gang.
	 */
	@Getter
	private final OfflinePlayer player;

	/**
	 * The gang the player is joining.
	 */
	@Getter
	private final Gang gang;

	/**
	 * Constructs a new {@link GangJoinEvent}.
	 *
	 * @param player the {@link OfflinePlayer} who joined the gang
	 * @param gang   the {@link Gang} being joined
	 */
	public GangJoinEvent(OfflinePlayer player, Gang gang) {
		super(player);
		this.player = player;
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
