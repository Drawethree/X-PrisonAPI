package dev.drawethree.xprison.api.autominer.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a player upgrades their AutoMiner tier.
 * <p>
 * Fired after the upgrade cost has been charged but before the new tier is
 * applied. If cancelled, the charged cost is refunded and the tier is unchanged.
 */
@Getter
public final class PlayerAutoMinerTierUpgradeEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * Whether this event is cancelled.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * The tier the player is upgrading from.
	 */
	private final int fromTier;

	/**
	 * The tier the player is upgrading to.
	 */
	private final int toTier;

	/**
	 * Constructs a new PlayerAutoMinerTierUpgradeEvent.
	 *
	 * @param player   The player upgrading their AutoMiner
	 * @param fromTier The current tier
	 * @param toTier   The tier being upgraded to
	 */
	public PlayerAutoMinerTierUpgradeEvent(Player player, int fromTier, int toTier) {
		super(player);
		this.fromTier = fromTier;
		this.toTier = toTier;
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
