package dev.drawethree.xprison.api.autominer.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Event called when a player's AutoMiner time is modified.
 */
@Setter
@Getter
public final class PlayerAutoMinerTimeModifyEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * Unit of time for the duration modification (e.g., seconds, minutes).
	 */
	private final TimeUnit timeUnit;

	/**
	 * Duration to modify the AutoMiner time by. Can be negative to reduce time.
	 */
	private final long duration;

	/**
	 * Constructs a new PlayerAutoMinerTimeModifyEvent.
	 *
	 * @param player   The player whose AutoMiner time is being modified
	 * @param unit     The unit of time for the modification
	 * @param duration The amount of time to add or subtract (can be negative)
	 */
	public PlayerAutoMinerTimeModifyEvent(Player player, TimeUnit unit, long duration) {
		super(player);
		this.timeUnit = unit;
		this.duration = duration;
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return The HandlerList for this event.
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return The HandlerList for this event.
	 */
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
