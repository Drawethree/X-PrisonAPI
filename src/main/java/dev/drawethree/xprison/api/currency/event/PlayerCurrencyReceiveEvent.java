package dev.drawethree.xprison.api.currency.event;

import dev.drawethree.xprison.api.currency.enums.ReceiveCause;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is about to receive currency.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the player from receiving the currency.
 * You can also modify the amount to be received via {@link #setAmount(double)}.
 */
@Getter
public final class PlayerCurrencyReceiveEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The currency.
	 */
	private final XPrisonCurrency currency;

	/**
	 * The cause of why the player is receiving currency.
	 */
	private final ReceiveCause cause;

	/**
	 * The amount of currency the player will receive.
	 * Can be modified using {@link #setAmount(double)}.
	 */
	@Setter
	private double amount;

	/**
	 * Whether the event is cancelled.
	 * If set to {@code true}, the player will not receive the currency.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new {@link PlayerCurrencyReceiveEvent}.
	 *
	 * @param currency the {@link XPrisonCurrency}
	 * @param cause  the {@link ReceiveCause} indicating why the currency is being granted
	 * @param player the {@link OfflinePlayer} receiving the currency
	 * @param amount the amount of currency to be received
	 */
	public PlayerCurrencyReceiveEvent(XPrisonCurrency currency, ReceiveCause cause, OfflinePlayer player, long amount) {
		super(player);
		this.currency = currency;
		this.cause = cause;
		this.amount = amount;
	}

	/**
	 * Gets the static handler list for {@link PlayerCurrencyReceiveEvent}.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handlers for this specific event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
