package dev.drawethree.xprison.api.autominer.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a player performs an auto mine action in an AutoMiner region.
 */
@Getter
public final class PlayerAutomineEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * Whether this event is cancelled.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Remaining autominer time for the player in seconds.
	 */
	private final int timeLeft;

	/**
	 * Constructs a new PlayerAutomineEvent.
	 *
	 * @param player   The player performing the automine action
	 * @param timeLeft Remaining autominer time in seconds
	 */
	public PlayerAutomineEvent(Player player, int timeLeft) {
		super(player);
		this.timeLeft = timeLeft;
	}

	/**
	 * Gets the handler list for this event class.
	 *
	 * @return HandlerList for this event
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return HandlerList for this event
	 */
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
