package dev.drawethree.xprison.api.milestones.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * How a milestone looks in the menu: what it says it asks for, what it says it pays, and the icon
 * it is drawn with.
 * <p>
 * Nothing here is the reward itself - {@link #getRewardLines()} is text a server owner wrote to
 * describe {@link Milestone#getCommands()}, and the two can disagree.
 *
 * @since 1.9
 */
public interface MilestoneDisplay {

	/**
	 * Gets the description shown for this milestone, which replaces the requirement line the menu
	 * would otherwise write itself.
	 *
	 * @return the description, or an empty string if none is configured
	 */
	@NotNull
	String getDescription();

	/**
	 * Gets the human-readable reward lines shown for this milestone.
	 *
	 * @return an unmodifiable list of reward lines, possibly empty
	 */
	@NotNull
	List<String> getRewardLines();

	/**
	 * Gets the material name used for this milestone's icon, overriding the icon the menu would
	 * otherwise pick from whether it is claimed.
	 *
	 * @return the material name, or an empty string to use the menu default
	 */
	@NotNull
	String getMaterial();

	/**
	 * Gets the custom model data applied to this milestone's icon.
	 *
	 * @return the custom model data, or {@code 0} if none is configured
	 */
	int getCustomModelData();
}
