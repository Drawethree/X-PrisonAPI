package dev.drawethree.xprison.api.bombs.events;

import dev.drawethree.xprison.api.bombs.model.Bomb;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player throws a bomb, before its explosion timer starts.
 * <p>
 * This event is {@link Cancellable}; cancelling it prevents the thrown bomb from arming
 * (the {@link BombExplodeEvent} will not fire).
 */
@Getter
public final class BombThrowEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private final Bomb bomb;

	private final Location location;

	/**
	 * Constructs a new {@link BombThrowEvent}.
	 *
	 * @param player   the player throwing the bomb
	 * @param bomb     the bomb being thrown
	 * @param location the location the bomb was thrown at
	 */
	public BombThrowEvent(Player player, Bomb bomb, Location location) {
		super(player);
		this.bomb = bomb;
		this.location = location;
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
