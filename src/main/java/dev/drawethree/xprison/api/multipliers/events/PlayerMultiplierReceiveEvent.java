package dev.drawethree.xprison.api.multipliers.events;

import dev.drawethree.xprison.api.multipliers.model.MultiplierType;
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

	private final double multiplier;

	private final TimeUnit timeUnit;

	private final long duration;

	private final MultiplierType type;

	/**
	 * Constructs a new PlayerMultiplierReceiveEvent.
	 *
	 * @param player     the player receiving the multiplier
	 * @param multiplier the multiplier value
	 * @param timeUnit   the unit of time for the multiplier duration
	 * @param duration   the duration of the multiplier
	 * @param type       the type of multiplier (e.g., SELL, TOKENS)
	 */
	public PlayerMultiplierReceiveEvent(Player player, double multiplier, TimeUnit timeUnit, long duration, MultiplierType type) {
		super(player);
		this.multiplier = multiplier;
		this.timeUnit = timeUnit;
		this.duration = duration;
		this.type = type;
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
