package dev.drawethree.xprison.api.season.events;

import dev.drawethree.xprison.api.season.SeasonResetResult;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import org.bukkit.event.HandlerList;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fired on the main thread after a season reset has finished and online players' data has been
 * reloaded.
 *
 * @since 1.10
 */
public final class SeasonResetCompleteEvent extends XPrisonEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Map<String, SeasonResetResult> results;

	/**
	 * Constructs a new {@link SeasonResetCompleteEvent}.
	 *
	 * @param results the outcome per participant id, in the order they ran
	 */
	public SeasonResetCompleteEvent(Map<String, SeasonResetResult> results) {
		this.results = new LinkedHashMap<>(results);
	}

	/**
	 * Gets the outcome of every participant that ran, in run order.
	 *
	 * @return the results by participant id
	 */
	public Map<String, SeasonResetResult> getResults() {
		return this.results;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
