package dev.drawethree.xprison.api.dailyrewards.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Fired after a player claims their daily login reward.
 */
@Getter
public final class PlayerDailyRewardClaimEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final int streak;
	private final int cycleDay;

	/**
	 * @param player   the claiming player
	 * @param streak   the player's streak after this claim
	 * @param cycleDay the 1-based cycle day that was claimed
	 */
	public PlayerDailyRewardClaimEvent(Player player, int streak, int cycleDay) {
		super(player);
		this.streak = streak;
		this.cycleDay = cycleDay;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
