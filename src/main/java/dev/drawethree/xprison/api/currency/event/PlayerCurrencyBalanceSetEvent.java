package dev.drawethree.xprison.api.currency.event;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player's balance of a currency is about to be set to an explicit value
 * (i.e. {@link dev.drawethree.xprison.api.currency.XPrisonCurrencyAPI#setBalance}).
 * <p>
 * Unlike {@link PlayerCurrencyReceiveEvent} / {@link PlayerCurrencyLoseEvent}, this fires for an
 * absolute assignment rather than a delta. It is {@link Cancellable}; cancelling leaves the existing
 * balance untouched. The target value can be adjusted via {@link #setNewAmount(double)}.
 */
@Getter
public final class PlayerCurrencyBalanceSetEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The currency being set.
	 */
	private final XPrisonCurrency currency;

	/**
	 * The player's balance before the set operation.
	 */
	private final double oldAmount;

	/**
	 * The balance the player will be set to.
	 * Can be modified using {@link #setNewAmount(double)}.
	 */
	@Setter
	private double newAmount;

	/**
	 * Whether the event is cancelled.
	 * If {@code true}, the balance is left unchanged.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new {@link PlayerCurrencyBalanceSetEvent}.
	 *
	 * @param currency  the {@link XPrisonCurrency} whose balance is being set
	 * @param player    the {@link OfflinePlayer} whose balance is changing
	 * @param oldAmount the balance before the set operation
	 * @param newAmount the balance the player will be set to
	 */
	public PlayerCurrencyBalanceSetEvent(XPrisonCurrency currency, OfflinePlayer player, double oldAmount, double newAmount) {
		super(player);
		this.currency = currency;
		this.oldAmount = oldAmount;
		this.newAmount = newAmount;
	}

	/**
	 * Gets the static handler list for {@link PlayerCurrencyBalanceSetEvent}.
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
