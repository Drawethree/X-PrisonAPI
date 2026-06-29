package dev.drawethree.xprison.api.battlepass.model;

/**
 * Represents a single tier of a Battle Pass season, with the XP needed to reach it
 * and the rewards available on each {@link PassTrack}.
 */
public interface PassTier {

	/**
	 * Gets the 1-based tier number.
	 *
	 * @return the tier number
	 */
	int getTier();

	/**
	 * Gets the amount of XP required to advance into this tier from the previous one.
	 *
	 * @return the incremental XP requirement for this tier
	 */
	long getRequiredXp();

	/**
	 * Gets the total cumulative XP required, from the start of the season, to reach this tier.
	 *
	 * @return the cumulative XP requirement
	 */
	long getCumulativeXp();

	/**
	 * Checks whether this tier grants any reward on the given track.
	 *
	 * @param track the track to check
	 * @return {@code true} if a reward is configured for the track at this tier
	 */
	boolean hasReward(PassTrack track);
}
