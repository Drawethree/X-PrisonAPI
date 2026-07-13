package dev.drawethree.xprison.api.currency.event;

import dev.drawethree.xprison.api.currency.enums.LostCause;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Called when a player loses a currency amount (e.g., gems, tokens).
 * <p>
 * This event is useful for logging, reacting to, or modifying the loss of gems or other currencies due to specific causes.
 * The amount can be modified using {@link #setAmount(double)} or, for OP-scale exact amounts,
 * {@link #setAmountExact(BigDecimal)}.
 */
@Getter
public final class PlayerCurrencyLoseEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The currency.
	 */
	private final XPrisonCurrency currency;

	/**
	 * The cause for which the player is losing currency.
	 */
	private final LostCause cause;

	/**
	 * The exact amount of currency the player is losing (source of truth).
	 * Can be modified using {@link #setAmount(double)} or {@link #setAmountExact(BigDecimal)}.
	 */
	@Getter(AccessLevel.NONE)
	private BigDecimal amount;

	/**
	 * Constructs a new {@link PlayerCurrencyLoseEvent}.
	 *
	 * @param currency the {@link XPrisonCurrency}
	 * @param cause  the {@link LostCause} representing why the currency is being lost
	 * @param player the {@link OfflinePlayer} who is losing currency
	 * @param amount the amount of currency being lost
	 */
	public PlayerCurrencyLoseEvent(XPrisonCurrency currency, LostCause cause, OfflinePlayer player, double amount) {
		this(currency, cause, player, CurrencyAmounts.exact(amount));
	}

	/**
	 * Constructs a new {@link PlayerCurrencyLoseEvent} with an exact amount.
	 *
	 * @param currency the {@link XPrisonCurrency}
	 * @param cause  the {@link LostCause} representing why the currency is being lost
	 * @param player the {@link OfflinePlayer} who is losing currency
	 * @param amount the exact amount of currency being lost
	 */
	public PlayerCurrencyLoseEvent(XPrisonCurrency currency, LostCause cause, OfflinePlayer player, BigDecimal amount) {
		super(player);
		this.currency = currency;
		this.cause = cause;
		this.amount = amount;
	}

	/**
	 * @return the amount the player is losing (loses precision above 2^53; prefer
	 *         {@link #getAmountExact()})
	 */
	public double getAmount() {
		return amount.doubleValue();
	}

	/**
	 * @return the exact amount the player is losing
	 */
	public BigDecimal getAmountExact() {
		return amount;
	}

	/**
	 * Sets the amount the player is losing.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(double amount) {
		this.amount = CurrencyAmounts.exact(amount);
	}

	/**
	 * Sets the exact amount the player is losing.
	 *
	 * @param amount the new exact amount
	 */
	public void setAmountExact(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Gets the static handler list for {@link PlayerCurrencyLoseEvent}.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this specific event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
