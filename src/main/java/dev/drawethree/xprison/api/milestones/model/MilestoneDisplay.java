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

	/**
	 * Gets the item model applied to the milestone icon, as a namespaced key such as {@code nexo:ruby_pickaxe}.
	 * <p>
	 * Only applied on servers that support the {@code item_model} item component (1.21.3+).
	 *
	 * @return the item model key, or {@code null} if none is configured
	 * @since 1.10
	 */
	default String getItemModel() {
		return null;
	}

	/**
	 * Gets the tooltip style applied to the milestone icon, as a namespaced key such as {@code nexo:legendary}.
	 * <p>
	 * Only applied on servers that support the {@code tooltip_style} item component (1.21.3+).
	 *
	 * @return the tooltip style key, or {@code null} if none is configured
	 * @since 1.10
	 */
	default String getTooltipStyle() {
		return null;
	}
}
