package dev.drawethree.xprison.api.quests.model;

/**
 * The objective kind a quest tracks. Each type is fed by a corresponding X-Prison event;
 * a quest may additionally narrow a type with a filter (e.g. a specific currency or enchant).
 */
public enum QuestType {

	/**
	 * Mine a number of blocks.
	 */
	MINE_BLOCKS,

	/**
	 * Earn an amount of a currency (optionally filtered to one currency by name).
	 */
	EARN_CURRENCY,

	/**
	 * Rank up a number of times.
	 */
	RANKUP,

	/**
	 * Prestige a number of times.
	 */
	PRESTIGE,

	/**
	 * Rebirth a number of times.
	 */
	REBIRTH,

	/**
	 * Trigger pickaxe enchants a number of times (optionally filtered to one enchant by name).
	 */
	TRIGGER_ENCHANT,

	/**
	 * Detonate a number of bombs.
	 */
	USE_BOMB,

	/**
	 * Join a gang.
	 */
	JOIN_GANG,

	/**
	 * Perform a number of auto-miner cycles.
	 */
	AUTOMINE
}
