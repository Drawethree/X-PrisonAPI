package dev.drawethree.xprison.api.quests.model;

/**
 * A quest assigned to a player, pairing the {@link Quest} definition with that player's
 * current progress and claim state.
 */
public interface ActiveQuest {

	/**
	 * Gets the underlying quest definition.
	 *
	 * @return the quest
	 */
	Quest getQuest();

	/**
	 * Gets the player's current progress towards the target.
	 *
	 * @return the current progress
	 */
	long getProgress();

	/**
	 * Gets the player's effective target for this quest — the configured base target after any
	 * per-player scaling has been applied. Equal to {@link Quest#getTargetAmount()} when the
	 * quest is not scaled.
	 *
	 * @return the effective (scaled) target this player must reach
	 */
	long getEffectiveTarget();

	/**
	 * Checks whether the target has been reached.
	 *
	 * @return {@code true} if the quest is completed
	 */
	boolean isCompleted();

	/**
	 * Checks whether the completed quest's reward has been claimed.
	 *
	 * @return {@code true} if the reward has been claimed
	 */
	boolean isClaimed();

	/**
	 * Gets progress as a fraction in the range {@code [0.0, 1.0]}.
	 *
	 * @return the completion fraction
	 */
	double getProgressFraction();
}
