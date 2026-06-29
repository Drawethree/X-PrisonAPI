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
