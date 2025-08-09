package dev.drawethree.xprison.api.rebirth.events;

import dev.drawethree.xprison.api.rebirth.model.Rebirth;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player rebirths.
 * <p>
 * Contains information about the old rebirth and the new rebirth.
 * This event is cancellable, allowing prevention of the rebirth.
 */
@Getter
public final class PlayerRebirthEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Rebirth oldRebirth;

	@Setter
	private Rebirth newRebirth;

	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new PlayerRebirthEvent.
	 *
	 * @param player Player who rebirth
	 * @param oldR   The player's old rebirth
	 * @param newR   The new rebirth the player is moving to
	 */
	public PlayerRebirthEvent(Player player, Rebirth oldR, Rebirth newR) {
		super(player);
		this.oldRebirth = oldR;
		this.newRebirth = newR;
	}

	/**
	 * Gets the HandlerList for this event.
	 *
	 * @return HandlerList instance
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
