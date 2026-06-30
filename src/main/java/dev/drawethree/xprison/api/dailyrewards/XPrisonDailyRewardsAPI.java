package dev.drawethree.xprison.api.dailyrewards;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * API interface for the Daily Rewards module.
 * <p>
 * Players claim one reward per day; consecutive days build a streak that unlocks escalating
 * cycle rewards and milestone bonuses. This API exposes a player's streak state and allows
 * triggering a claim.
 */
public interface XPrisonDailyRewardsAPI {

	/**
	 * Gets the player's current consecutive-day streak.
	 *
	 * @param playerUuid the player's unique id
	 * @return the current streak (0 if they have never claimed)
	 */
	int getStreak(@NotNull UUID playerUuid);

	/**
	 * Gets the longest streak the player has ever reached.
	 *
	 * @param playerUuid the player's unique id
	 * @return the longest streak
	 */
	int getLongestStreak(@NotNull UUID playerUuid);

	/**
	 * Gets the total number of daily rewards the player has ever claimed.
	 *
	 * @param playerUuid the player's unique id
	 * @return the total claim count
	 */
	long getTotalClaims(@NotNull UUID playerUuid);

	/**
	 * Checks whether the player currently has a daily reward available to claim.
	 *
	 * @param playerUuid the player's unique id
	 * @return {@code true} if a claim is available today
	 */
	boolean canClaim(@NotNull UUID playerUuid);

	/**
	 * Claims the player's daily reward if one is available. The player must be online to
	 * receive the reward.
	 *
	 * @param playerUuid the player's unique id
	 * @return {@code true} if a reward was claimed
	 */
	boolean claim(@NotNull UUID playerUuid);

	// ---------------------------------------------------------------------
	// Administration (config + offline-capable player data)
	// ---------------------------------------------------------------------

	/**
	 * Reloads the Daily Rewards configuration from {@code dailyrewards.yml}. Use after editing
	 * the config externally (e.g. from the web dashboard).
	 */
	void reloadConfig();

	/**
	 * Sets a player's current consecutive-day streak (clamped to {@code >= 0}). Works for
	 * offline players. The longest streak is raised to match if exceeded.
	 *
	 * @param playerUuid the player's unique id
	 * @param streak     the streak value to set
	 */
	void setStreak(@NotNull UUID playerUuid, int streak);

	/**
	 * Sets a player's longest-ever streak (clamped to {@code >= 0}). Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param streak     the longest-streak value to set
	 */
	void setLongestStreak(@NotNull UUID playerUuid, int streak);

	/**
	 * Sets a player's lifetime claim count (clamped to {@code >= 0}). Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param total      the total-claims value to set
	 */
	void setTotalClaims(@NotNull UUID playerUuid, long total);

	/**
	 * Clears a player's streak state (streak, longest, total and last-claim), making a claim
	 * available again. Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 */
	void resetPlayer(@NotNull UUID playerUuid);
}
