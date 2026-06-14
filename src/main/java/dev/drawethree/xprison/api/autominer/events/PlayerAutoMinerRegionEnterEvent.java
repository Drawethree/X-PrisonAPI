package dev.drawethree.xprison.api.autominer.events;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player enters an AutoMiner region.
 * <p>
 * This is a notification event and is not cancellable.
 */
@Getter
public final class PlayerAutoMinerRegionEnterEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The AutoMiner region the player entered.
	 */
	private final AutoMinerRegion region;

	/**
	 * Constructs a new PlayerAutoMinerRegionEnterEvent.
	 *
	 * @param player the player who entered the region
	 * @param region the AutoMiner region entered
	 */
	public PlayerAutoMinerRegionEnterEvent(Player player, AutoMinerRegion region) {
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
