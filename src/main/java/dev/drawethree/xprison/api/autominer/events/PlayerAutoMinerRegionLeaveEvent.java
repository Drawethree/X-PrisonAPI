package dev.drawethree.xprison.api.autominer.events;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player leaves an AutoMiner region.
 * <p>
 * This is a notification event and is not cancellable.
 */
@Getter
public final class PlayerAutoMinerRegionLeaveEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The AutoMiner region the player left.
	 */
	private final AutoMinerRegion region;

	/**
	 * Constructs a new PlayerAutoMinerRegionLeaveEvent.
	 *
	 * @param player the player who left the region
	 * @param region the AutoMiner region left
	 */
	public PlayerAutoMinerRegionLeaveEvent(Player player, AutoMinerRegion region) {
		super(player);
		this.region = region;
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
