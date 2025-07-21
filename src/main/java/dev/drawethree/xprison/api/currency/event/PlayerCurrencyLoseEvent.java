package dev.drawethree.xprison.api.currency.event;

import dev.drawethree.xprison.api.currency.enums.LostCause;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player loses a currency amount (e.g., gems, tokens).
 * <p>
 * This event is useful for logging, reacting to, or modifying the loss of gems or other currencies due to specific causes.
 * The amount can be modified using {@link #setAmount(double)}.
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
	 * The amount of currency the player is losing.
	 * Can be modified using {@link #setAmount(double)}.
	 */
	@Setter
	private double amount;

	/**
	 * Constructs a new {@link PlayerCurrencyLoseEvent}.
	 *
	 * @param currency the {@link XPrisonCurrency}
	 * @param cause  the {@link LostCause} representing why the currency is being lost
	 * @param player the {@link OfflinePlayer} who is losing currency
	 * @param amount the amount of currency being lost
	 */
	public PlayerCurrencyLoseEvent(XPrisonCurrency currency, LostCause cause, OfflinePlayer player, double amount) {
		super(player);
		this.currency = currency;
		this.cause = cause;
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
