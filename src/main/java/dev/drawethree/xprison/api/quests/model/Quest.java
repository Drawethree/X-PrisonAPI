package dev.drawethree.xprison.api.quests.model;

/**
 * Represents a quest definition (the template), independent of any player's progress.
 */
public interface Quest {

	/**
	 * Gets the unique id of this quest, as configured.
	 *
	 * @return the quest id
	 */
	String getId();

	/**
	 * Gets the objective type this quest tracks.
	 *
	 * @return the quest type
	 */
	QuestType getType();

	/**
	 * Gets the category controlling how this quest is assigned and rotated.
	 *
	 * @return the quest category
	 */
	QuestCategory getCategory();

	/**
	 * Gets the human-readable display name of this quest.
	 *
	 * @return the display name
	 */
	String getDisplayName();

	/**
	 * Gets the target amount required to complete this quest.
	 *
	 * @return the target amount
	 */
	long getTargetAmount();
}
