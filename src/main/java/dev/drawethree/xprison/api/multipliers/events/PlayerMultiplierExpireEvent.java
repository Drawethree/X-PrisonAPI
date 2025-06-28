package dev.drawethree.xprison.api.multipliers.events;

import dev.drawethree.xprison.api.multipliers.model.PlayerMultiplier;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @deprecated This event is deprecated and may be removed in future versions.
 *
 * Event called when a player's multiplier expires.
 */
@Getter
@Deprecated
public final class PlayerMultiplierExpireEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final PlayerMultiplier multiplier;

	/**
	 * Constructs a new PlayerMultiplierExpireEvent.
	 *
	 * @param player     the player whose multiplier expired
	 * @param multiplier the expired player multiplier
	 */
	public PlayerMultiplierExpireEvent(Player player, PlayerMultiplier multiplier) {
		super(player);
		this.multiplier = multiplier;
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
