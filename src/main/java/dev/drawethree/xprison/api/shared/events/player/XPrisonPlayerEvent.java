package dev.drawethree.xprison.api.shared.events.player;

import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Abstract base class for XPrison events related to a player.
 * Stores an OfflinePlayer reference.
 */
@Getter
public abstract class XPrisonPlayerEvent extends XPrisonEvent {

	protected OfflinePlayer player;

	/**
	 * Constructs a new player-related event.
	 *
	 * @param player the OfflinePlayer involved in this event
	 */
	public XPrisonPlayerEvent(OfflinePlayer player) {
		this.player = player;
	}

	public Player getPlayerOnline() {
		return player.getPlayer();
	}
}
