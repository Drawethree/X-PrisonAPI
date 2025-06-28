package dev.drawethree.xprison.api.gangs.enums;

/**
 * Enum representing possible results of checking a gang name validity.
 */
public enum GangNameCheckResult {

	/**
	 * Gang name is available and meets all criteria.
	 */
	SUCCESS,

	/**
	 * Gang name is restricted and cannot be used.
	 */
	NAME_RESTRICTED,

	/**
	 * Gang name is too long to be accepted.
	 */
	NAME_TOO_LONG,

	/**
	 * Gang name contains color codes, which are not allowed.
	 */
	NAME_CONTAINS_COLORS,

	/**
	 * Gang with such a name already exists.
	 */
	NAME_TAKEN,

	/**
	 * Gang name is empty or not provided.
	 */
	NAME_EMPTY,
}
