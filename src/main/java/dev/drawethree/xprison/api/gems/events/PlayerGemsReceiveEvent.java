package dev.drawethree.xprison.api.gems.events;

import dev.drawethree.xprison.api.shared.currency.enums.ReceiveCause;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event that is called when a player is about to receive gems.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the player from receiving gems.
 * You can also modify the amount of gems the player will receive via {@link #setAmount(long)}.
 */
@Getter
public final class PlayerGemsReceiveEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The cause of why the player is receiving gems.
	 */
	private final ReceiveCause cause;

	/**
	 * The amount of gems the player will receive.
	 * Can be modified using {@link #setAmount(long)}.
	 */
	@Setter
	private long amount;

	/**
	 * Whether the event is cancelled.
	 * If set to {@code true}, the player will not receive gems.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new {@link PlayerGemsReceiveEvent}.
	 *
	 * @param cause  the {@link ReceiveCause} indicating why the gems are being granted
	 * @param player the {@link OfflinePlayer} receiving the gems
	 * @param amount the amount of gems to be received
	 */
	public PlayerGemsReceiveEvent(ReceiveCause cause, OfflinePlayer player, long amount) {
		super(player);
		this.cause = cause;
		this.amount = amount;
	}

	/**
	 * Returns the list of handlers for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Returns the list of handlers for this event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
