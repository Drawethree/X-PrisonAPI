package dev.drawethree.xprison.api.boosters.events;

import dev.drawethree.xprison.api.boosters.model.Booster;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

/**
 * Fired on the main thread when a running booster has used up its time.
 *
 * @since 1.10
 */
@Getter
public final class PlayerBoosterExpireEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The booster that expired.
	 */
	private final Booster booster;

	/**
	 * Constructs a new {@link PlayerBoosterExpireEvent}.
	 *
	 * @param player  the booster's owner
	 * @param booster the booster that expired
	 */
	public PlayerBoosterExpireEvent(OfflinePlayer player, Booster booster) {
		super(player);
		this.booster = booster;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
