package dev.drawethree.xprison.api.battlepass.events;

import dev.drawethree.xprison.api.battlepass.model.PassTrack;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player is about to claim a Battle Pass tier reward.
 * <p>
 * This event is {@link Cancellable}; cancelling it prevents the reward from being granted
 * and marked as claimed.
 */
@Getter
public final class BattlePassRewardClaimEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private final int tier;

	private final PassTrack track;

	/**
	 * Constructs a new BattlePassRewardClaimEvent.
	 *
	 * @param player the player claiming the reward
	 * @param tier   the tier being claimed
	 * @param track  the track (free or premium) being claimed
	 */
	public BattlePassRewardClaimEvent(Player player, int tier, PassTrack track) {
		super(player);
		this.tier = tier;
		this.track = track;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
