package dev.drawethree.xprison.api.quests.model;

/**
 * How a quest is assigned and rotated.
 */
public enum QuestCategory {

	/**
	 * Rotates daily: a random selection from the daily pool is assigned each day.
	 */
	DAILY,

	/**
	 * Rotates weekly: a random selection from the weekly pool is assigned each week.
	 */
	WEEKLY,

	/**
	 * Always active and never rotated; progress is permanent until completed.
	 */
	PERMANENT
}
