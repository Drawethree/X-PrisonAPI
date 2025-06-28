package dev.drawethree.xprison.api.ranks.events;

import dev.drawethree.xprison.api.ranks.model.Rank;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player ranks up.
 * <p>
 * Contains information about the old rank and the new rank.
 * This event is cancellable, allowing prevention of the rank up.
 */
@Getter
public final class PlayerRankUpEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Rank oldRank;

	@Setter
	private Rank newRank;

	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new PlayerRankUpEvent.
	 *
	 * @param player Player who ranked up
	 * @param oldR   The player's old rank before ranking up
	 * @param newR   The new rank the player is moving to
	 */
	public PlayerRankUpEvent(Player player, Rank oldR, Rank newR) {
		super(player);
		this.oldRank = oldR;
		this.newRank = newR;
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
