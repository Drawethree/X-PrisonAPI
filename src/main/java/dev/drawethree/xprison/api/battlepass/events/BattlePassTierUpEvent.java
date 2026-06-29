package dev.drawethree.xprison.api.battlepass.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Fired after a player advances one or more Battle Pass tiers.
 * <p>
 * When a single XP gain crosses several tiers this event is fired once, with
 * {@link #getOldTier()} and {@link #getNewTier()} spanning the full jump.
 */
@Getter
public final class BattlePassTierUpEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final int oldTier;
	private final int newTier;

	/**
	 * Constructs a new BattlePassTierUpEvent.
	 *
	 * @param player  the player who advanced
	 * @param oldTier the tier before the advancement
	 * @param newTier the tier after the advancement
	 */
	public BattlePassTierUpEvent(Player player, int oldTier, int newTier) {
		super(player);
		this.oldTier = oldTier;
		this.newTier = newTier;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
