package dev.drawethree.xprison.api.battlepass.model;

/**
 * Identifies what caused a player to gain Battle Pass XP. Passed to
 * {@link dev.drawethree.xprison.api.battlepass.XPrisonBattlePassAPI#addXp} and
 * exposed on {@link dev.drawethree.xprison.api.battlepass.events.BattlePassXpGainEvent}
 * so integrations can react to, or filter, individual XP sources.
 */
public enum XpSource {

	/**
	 * XP granted for completing a quest.
	 */
	QUEST,

	/**
	 * XP granted for claiming a daily login reward.
	 */
	DAILY_REWARD,

	/**
	 * XP granted from mining activity.
	 */
	MINING,

	/**
	 * XP granted on prestige.
	 */
	PRESTIGE,

	/**
	 * XP granted on rebirth.
	 */
	REBIRTH,

	/**
	 * XP granted by an administrator or command.
	 */
	ADMIN,

	/**
	 * XP granted by any other source (e.g. an addon).
	 */
	OTHER
}
