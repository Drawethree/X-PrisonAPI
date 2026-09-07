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
 * <p>
 * The rung itself is the identity and the requirement; what the player sees lives on
 * {@link #getDisplay()} and {@link #getAnnouncement()}, and the payout is {@link #getCommands()}.
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
	 * Gets the console commands run when the milestone is reached. These are the actual reward;
	 * {@link MilestoneDisplay#getRewardLines()} only describes them.
	 *
	 * @return an unmodifiable list of commands, possibly empty
	 */
	@NotNull
	List<String> getCommands();

	/**
	 * Gets how this milestone is drawn in the menu.
	 *
	 * @return the display settings
	 */
	@NotNull
	MilestoneDisplay getDisplay();

	/**
	 * Gets how reaching this milestone is announced.
	 *
	 * @return the announcement settings
	 */
	@NotNull
	MilestoneAnnouncement getAnnouncement();
}
