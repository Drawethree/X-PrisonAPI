package dev.drawethree.xprison.api.battlepass.events;

import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired after a player's Battle Pass premium status is changed (e.g. by an admin or a store
 * addon granting/revoking premium).
 * <p>
 * This is an informational event and is not cancellable. It may fire off the main thread,
 * mirroring the offline-capable {@code setPremium} write path.
 */
@Getter
public final class BattlePassPremiumChangeEvent extends XPrisonEvent {

	private static final HandlerList handlers = new HandlerList();

	private final UUID uuid;

	private final boolean premium;

	/**
	 * Constructs a new BattlePassPremiumChangeEvent.
	 *
	 * @param uuid    the UUID of the affected player
	 * @param premium the new premium status
	 */
	public BattlePassPremiumChangeEvent(UUID uuid, boolean premium) {
		this.uuid = uuid;
		this.premium = premium;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
