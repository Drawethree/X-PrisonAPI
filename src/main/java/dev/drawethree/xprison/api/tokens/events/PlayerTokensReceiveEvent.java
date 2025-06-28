package dev.drawethree.xprison.api.tokens.events;

import dev.drawethree.xprison.api.shared.currency.enums.ReceiveCause;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player receives tokens.
 */
@Getter
public final class PlayerTokensReceiveEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The cause/reason why the player received tokens.
	 */
	private final ReceiveCause cause;

	/**
	 * The amount of tokens received by the player.
	 */
	@Setter
	private long amount;

	/**
	 * Whether this event is cancelled.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new PlayerTokensReceiveEvent.
	 *
	 * @param cause  the cause of token reception
	 * @param player the player who received tokens
	 * @param amount the amount of tokens received
	 */
	public PlayerTokensReceiveEvent(ReceiveCause cause, OfflinePlayer player, long amount) {
		super(player);
		this.cause = cause;
		this.amount = amount;
	}

	/**
	 * Gets the HandlerList for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
