package dev.drawethree.xprison.api.milestones.model;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * A single rung of a milestone ladder: a threshold on one {@link MilestoneTrack} together with
 * everything that happens when a player reaches it.
 * <p>
 * Milestones are defined in {@code milestones.yml} and are read-only through the API. A milestone
 * pays out the first time it is reached and never again, even if the underlying statistic is later
 * reset - a rebirth does not hand back the prestige ladder.
 */
public interface Milestone {

	/**
	 * Gets the milestone's id, which is its key in {@code milestones.yml} and is unique across
	 * every ladder.
	 *
	 * @return the milestone id
	 */
	@NotNull
	String getId();

	/**
	 * Gets the ladder this milestone belongs to.
	 *
	 * @return the track this milestone is measured on
	 */
	@NotNull
	MilestoneTrack getTrack();

	/**
	 * Gets the value of the tracked statistic at which this milestone is reached.
	 *
	 * @return the threshold, or a negative value if the milestone is misconfigured
	 */
	@NotNull
	BigDecimal getThreshold();

	/**
	 * Checks whether this milestone only matches an exact value.
	 * <p>
	 * An exact milestone fires once, when the statistic first reaches or passes its threshold.
	 * A non-exact one keeps firing on every further increase past the threshold, which is how
	 * "every prestige from here on" style rewards are configured.
	 *
	 * @return {@code true} if the milestone is exact
	 */
	boolean isExact();

	/**
	 * Gets the title shown to the player when the milestone is reached.
	 *
	 * @return the title, or an empty string if none is configured
	 */
	@NotNull
	String getTitle();

	/**
	 * Gets the subtitle shown to the player when the milestone is reached.
	 *
	 * @return the subtitle, or an empty string if none is configured
	 */
	@NotNull
	String getSubtitle();

	/**
	 * Gets the chat message sent to the player when the milestone is reached.
	 *
	 * @return the message, or an empty string if none is configured
	 */
	@NotNull
	String getMessage();

	/**
	 * Gets the message broadcast to everyone when the milestone is reached.
	 *
	 * @return the broadcast, or an empty string if none is configured
	 */
	@NotNull
	String getBroadcast();

	/**
	 * Gets the name of the sound played to the player when the milestone is reached.
	 *
	 * @return the sound name, or an empty string if none is configured
	 */
	@NotNull
	String getSound();

	/**
	 * Checks whether a firework is spawned when the milestone is reached.
	 *
	 * @return {@code true} if a firework is spawned
	 */
	boolean isFirework();

	/**
	 * Gets the console commands run when the milestone is reached. These are the actual reward;
	 * {@link #getRewards()} only describes them.
	 *
	 * @return an unmodifiable list of commands, possibly empty
	 */
	@NotNull
	List<String> getCommands();

	/**
	 * Gets the description shown for this milestone in the menu.
	 *
	 * @return the description, or an empty string if none is configured
	 */
	@NotNull
	String getDescription();

	/**
	 * Gets the human-readable reward lines shown for this milestone in the menu.
	 *
	 * @return an unmodifiable list of reward lines, possibly empty
	 */
	@NotNull
	List<String> getRewards();

	/**
	 * Gets the material name used for this milestone's menu icon, overriding the icon the menu
	 * would otherwise pick.
	 *
	 * @return the material name, or an empty string to use the menu default
	 */
	@NotNull
	String getMaterial();

	/**
	 * Gets the custom model data applied to this milestone's menu icon.
	 *
	 * @return the custom model data, or {@code 0} if none is configured
	 */
	int getCustomModelData();
}
