package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player is about to be teleported into a mine.
 * <p>
 * This event is {@link Cancellable}; cancelling it prevents the teleport.
 */
@Getter
public final class MineTeleportEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	private final Mine mine;

	/**
	 * Constructs a new {@link MineTeleportEvent}.
	 *
	 * @param player the player being teleported
	 * @param mine   the {@link Mine} the player is being teleported into
	 */
	public MineTeleportEvent(Player player, Mine mine) {
		super(player);
		this.mine = mine;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
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
}
