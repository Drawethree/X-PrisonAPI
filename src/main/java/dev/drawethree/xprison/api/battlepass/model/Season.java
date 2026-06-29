package dev.drawethree.xprison.api.battlepass.model;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a single Battle Pass season: a bounded period during which players
 * earn XP and climb tiers. When a season ends a new one begins and progress resets.
 */
public interface Season {

	/**
	 * Gets the unique, stable identifier of this season (e.g. {@code "season-1"}).
	 *
	 * @return the season id
	 */
	String getId();

	/**
	 * Gets the human-readable display name of this season.
	 *
	 * @return the display name
	 */
	String getDisplayName();

	/**
	 * Gets the date on which this season starts, if configured.
	 *
	 * @return the start date, or empty if the season has no fixed start
	 */
	Optional<LocalDate> getStartDate();

	/**
	 * Gets the date on which this season ends, if configured.
	 *
	 * @return the end date, or empty if the season has no fixed end
	 */
	Optional<LocalDate> getEndDate();

	/**
	 * Gets the highest tier obtainable in this season.
	 *
	 * @return the maximum tier (inclusive)
	 */
	int getMaxTier();

	/**
	 * Checks whether this season is currently active based on its configured dates.
	 * A season with no start or end date is always considered active.
	 *
	 * @return {@code true} if the current date falls within this season
	 */
	boolean isActive();
}
