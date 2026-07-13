package dev.drawethree.xprison.api.currency.event;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Event fired when a player pays currency to another player, as a single transaction.
 * <p>
 * This wraps the player-to-player pay/transfer: cancelling it prevents both the deduction
 * from the sender and the credit to the receiver (the individual
 * {@code PlayerCurrencyLoseEvent} / {@code PlayerCurrencyReceiveEvent} still fire for the
 * two sides once the pay proceeds). The paid {@code amount} may be adjusted via
 * {@link #setAmount(double)} or {@link #setAmountExact(BigDecimal)} — the sender is re-checked for
 * sufficient balance afterwards.
 */
@Getter
public final class PlayerCurrencyPayEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private final OfflinePlayer sender;

	private final OfflinePlayer receiver;

	private final XPrisonCurrency currency;

	@Getter(AccessLevel.NONE)
	private BigDecimal amount;

	/**
	 * Constructs a new {@link PlayerCurrencyPayEvent}.
	 *
	 * @param sender   the player sending the currency
	 * @param receiver the player receiving the currency
	 * @param currency the currency being paid
	 * @param amount   the amount being paid
	 */
	public PlayerCurrencyPayEvent(OfflinePlayer sender, OfflinePlayer receiver, XPrisonCurrency currency, double amount) {
		this(sender, receiver, currency, CurrencyAmounts.exact(amount));
	}

	/**
	 * Constructs a new {@link PlayerCurrencyPayEvent} with an exact amount.
	 *
	 * @param sender   the player sending the currency
	 * @param receiver the player receiving the currency
	 * @param currency the currency being paid
	 * @param amount   the exact amount being paid
	 */
	public PlayerCurrencyPayEvent(OfflinePlayer sender, OfflinePlayer receiver, XPrisonCurrency currency, BigDecimal amount) {
		this.sender = sender;
		this.receiver = receiver;
		this.currency = currency;
		this.amount = amount;
	}

	/**
	 * @return the amount being paid (loses precision above 2^53; prefer {@link #getAmountExact()})
	 */
	public double getAmount() {
		return amount.doubleValue();
	}

	/**
	 * @return the exact amount being paid
	 */
	public BigDecimal getAmountExact() {
		return amount;
	}

	/**
	 * Sets the amount being paid.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(double amount) {
		this.amount = CurrencyAmounts.exact(amount);
	}

	/**
	 * Sets the exact amount being paid.
	 *
	 * @param amount the new exact amount
	 */
	public void setAmountExact(BigDecimal amount) {
		this.amount = amount;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
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
