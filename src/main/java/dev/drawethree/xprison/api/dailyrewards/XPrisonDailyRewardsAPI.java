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
}
