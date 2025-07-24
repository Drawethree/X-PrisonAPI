package dev.drawethree.xprison.api.multipliers.events;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Event called when a player receives a multiplier.
 */
@Getter
public final class PlayerMultiplierReceiveEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final XPrisonCurrency currency;

	private final double multiplier;

	private final TimeUnit timeUnit;

	private final long duration;


	/**
	 * Constructs a new PlayerMultiplierReceiveEvent.
	 *
	 * @param player     the player receiving the multiplier
	 * @param multiplier the multiplier value
	 * @param timeUnit   the unit of time for the multiplier duration
	 * @param duration   the duration of the multiplier
	 * @param currency       the currency affected
	 */
	public PlayerMultiplierReceiveEvent(Player player, XPrisonCurrency currency, double multiplier, TimeUnit timeUnit, long duration) {
		super(player);
        this.currency = currency;
        this.multiplier = multiplier;
		this.timeUnit = timeUnit;
		this.duration = duration;
	}

	/**
	 * Gets the list of handlers listening to this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
