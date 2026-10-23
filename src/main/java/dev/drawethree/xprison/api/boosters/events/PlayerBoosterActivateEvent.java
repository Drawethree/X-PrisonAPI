package dev.drawethree.xprison.api.boosters.events;

import dev.drawethree.xprison.api.boosters.model.Booster;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

/**
 * Fired on the main thread when a booster starts running - right after it is queued with nothing
 * else running for its target, or when the booster before it expires.
 *
 * @since 1.10
 */
@Getter
public final class PlayerBoosterActivateEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The booster that started running.
	 */
	private final Booster booster;

	/**
	 * Constructs a new {@link PlayerBoosterActivateEvent}.
	 *
	 * @param player  the booster's owner
	 * @param booster the booster that started running
	 */
	public PlayerBoosterActivateEvent(OfflinePlayer player, Booster booster) {
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
