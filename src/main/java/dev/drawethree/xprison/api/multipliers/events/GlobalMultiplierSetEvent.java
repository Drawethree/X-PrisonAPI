package dev.drawethree.xprison.api.multipliers.events;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Event fired when a global (server-wide) multiplier is set for a currency.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the global multiplier
 * from being set. The {@code multiplier} value may be adjusted via {@link #setMultiplier(double)}.
 */
@Getter
public final class GlobalMultiplierSetEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private final XPrisonCurrency currency;

	@Setter
	private double multiplier;

	private final TimeUnit timeUnit;

	private final int duration;

	/**
	 * Constructs a new {@link GlobalMultiplierSetEvent}.
	 *
	 * @param currency   the currency affected
	 * @param multiplier the multiplier value being set
	 * @param timeUnit   the unit of time for the multiplier duration
	 * @param duration   the duration of the multiplier
	 */
	public GlobalMultiplierSetEvent(XPrisonCurrency currency, double multiplier, TimeUnit timeUnit, int duration) {
		this.currency = currency;
		this.multiplier = multiplier;
		this.timeUnit = timeUnit;
		this.duration = duration;
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
