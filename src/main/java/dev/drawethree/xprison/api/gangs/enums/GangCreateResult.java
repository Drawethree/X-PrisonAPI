package dev.drawethree.xprison.api.gangs.enums;

/**
 * Enum representing possible results of an attempt to create a gang.
 */
public enum GangCreateResult {

	/**
	 * Gang was created successfully.
	 */
	SUCCESS,

	/**
	 * Gang name is restricted and cannot be used.
	 */
	NAME_RESTRICTED,

	/**
	 * Gang name is too long.
	 */
	NAME_TOO_LONG,

	/**
	 * Gang name contains color codes, which are not allowed.
	 */
	NAME_CONTAINS_COLORS,

	/**
	 * A gang with such a name already exists.
	 */
	NAME_TAKEN,

	/**
	 * Gang name was not provided or is empty.
	 */
	NAME_EMPTY,

	/**
	 * The player attempting to create a gang already belongs to one.
	 */
	PLAYER_HAS_GANG,

	/**
	 * The creation event was cancelled during event handling.
	 */
	EVENT_CANCELLED,
}
