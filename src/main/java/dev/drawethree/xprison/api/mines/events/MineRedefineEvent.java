package dev.drawethree.xprison.api.mines.events;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a mine's region (bounds) is about to be redefined via a new selection.
 * <p>
 * This event is {@link Cancellable}; cancelling it leaves the mine's region unchanged.
 * The {@link #getPlayer()} is the player who made the new selection.
 */
@Getter
public final class MineRedefineEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	private final Mine mine;

	/**
	 * Constructs a new {@link MineRedefineEvent}.
	 *
	 * @param player the player redefining the mine
	 * @param mine   the {@link Mine} being redefined
	 */
	public MineRedefineEvent(Player player, Mine mine) {
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
