package dev.drawethree.xprison.api.milestones.model;

/**
 * The player statistic a milestone ladder is measured against.
 * <p>
 * Every milestone in {@code milestones.yml} declares one of these under its {@code type} key,
 * and each type forms an independent ladder with its own progress.
 */
public enum MilestoneType {

	/**
	 * The numeric id of the player's rank.
	 */
	RANK("rank"),

	/**
	 * The numeric id of the player's prestige.
	 */
	PRESTIGE("prestige"),

	/**
	 * The numeric id of the player's rebirth.
	 */
	REBIRTH("rebirth"),

	/**
	 * The level of the pickaxe the player is holding.
	 */
	PICKAXE_LEVEL("pickaxe-level"),

	/**
	 * The player's lifetime broken-block count.
	 */
	BLOCKS("blocks"),

	/**
	 * The player's total playtime, in minutes.
	 */
	PLAYTIME("playtime");

	private final String key;

	MilestoneType(String key) {
		this.key = key;
	}

	/**
	 * Gets the key this type is written as in {@code milestones.yml}.
	 *
	 * @return the config key, for example {@code pickaxe-level}
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Resolves a type from its config key, ignoring case and surrounding whitespace.
	 *
	 * @param raw the key to resolve
	 * @return the matching type, or {@code null} if the key is unknown or {@code null}
	 */
	public static MilestoneType fromKey(String raw) {
		if (raw == null) {
			return null;
		}
		for (MilestoneType type : values()) {
			if (type.key.equalsIgnoreCase(raw.trim())) {
				return type;
			}
		}
		return null;
	}
}
