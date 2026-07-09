package dev.drawethree.xprison.api.autosell.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player's AutoSell preference is about to change (toggle, enable or disable).
 * <p>
 * This event is {@link Cancellable}; cancelling it keeps the player's current AutoSell state.
 * {@link #isNewState()} is the state AutoSell would be set to ({@code true} = enabled).
 */
@Getter
public final class AutoSellToggleEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	/**
	 * The AutoSell state the player would be set to ({@code true} = enabled).
	 */
	private final boolean newState;

	/**
	 * Constructs a new {@link AutoSellToggleEvent}.
	 *
	 * @param player   the player whose AutoSell preference is changing
	 * @param newState the state AutoSell would be set to ({@code true} = enabled)
	 */
	public AutoSellToggleEvent(Player player, boolean newState) {
		super(player);
		this.newState = newState;
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
