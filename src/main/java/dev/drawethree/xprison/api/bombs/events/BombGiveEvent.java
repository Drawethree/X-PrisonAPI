package dev.drawethree.xprison.api.bombs.events;

import dev.drawethree.xprison.api.bombs.model.Bomb;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player is given bomb item(s).
 * <p>
 * This event is {@link Cancellable}; cancelling it prevents the bomb(s) from being given.
 */
@Getter
public final class BombGiveEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private final Bomb bomb;

	private final int amount;

	/**
	 * Constructs a new {@link BombGiveEvent}.
	 *
	 * @param player the player receiving the bomb(s)
	 * @param bomb   the bomb being given
	 * @param amount the amount of bombs being given
	 */
	public BombGiveEvent(Player player, Bomb bomb, int amount) {
		super(player);
		this.bomb = bomb;
		this.amount = amount;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
