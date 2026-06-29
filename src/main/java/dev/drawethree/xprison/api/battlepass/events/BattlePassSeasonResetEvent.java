package dev.drawethree.xprison.api.battlepass.events;

import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;

/**
 * Fired when the active Battle Pass season is reset and rolled over to a new one.
 * All player progress for the previous season has been archived/cleared by the time
 * this event fires.
 */
@Getter
public final class BattlePassSeasonResetEvent extends XPrisonEvent {

	private static final HandlerList handlers = new HandlerList();

	private final String oldSeasonId;
	private final String newSeasonId;

	/**
	 * Constructs a new BattlePassSeasonResetEvent.
	 *
	 * @param oldSeasonId the id of the season that just ended
	 * @param newSeasonId the id of the season now starting
	 */
	public BattlePassSeasonResetEvent(String oldSeasonId, String newSeasonId) {
		this.oldSeasonId = oldSeasonId;
		this.newSeasonId = newSeasonId;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
