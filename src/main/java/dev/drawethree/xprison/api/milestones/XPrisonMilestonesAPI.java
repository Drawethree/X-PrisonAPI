package dev.drawethree.xprison.api.milestones;

import dev.drawethree.xprison.api.milestones.progress.MilestoneProgress;
import dev.drawethree.xprison.api.milestones.registry.MilestoneRegistry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * API entry point for the Milestones module.
 * <p>
 * Milestones are long ladders of goals across independent tracks - prestige, rebirth, blocks
 * broken, playtime and whatever else a plugin registers. Each rung pays out the first time a
 * player reaches it and never again, which is tracked as a per-track high-water mark that survives
 * a prestige or rebirth reset.
 * <p>
 * The two halves are separate: {@link #getRegistry()} is what exists, {@link #getProgress()} is
 * where a player stands. Depend on the half you need rather than on this interface.
 *
 * @since 1.9
 */
public interface XPrisonMilestonesAPI {

	/**
	 * Checks whether milestones are switched on in {@code milestones.yml}. While off, nothing is
	 * awarded, though progress queries still answer.
	 *
	 * @return {@code true} if milestones are enabled
	 */
	boolean isEnabled();

	/**
	 * Gets the ladders and the track registry - what milestones exist, and how to add a track of
	 * your own.
	 *
	 * @return the milestone registry
	 */
	@NotNull
	MilestoneRegistry getRegistry();

	/**
	 * Gets a player's standing on the ladders, and the high-water marks behind it.
	 *
	 * @return the progress view
	 */
	@NotNull
	MilestoneProgress getProgress();

	/**
	 * Reloads the Milestones configuration from {@code milestones.yml}, ladders and menu alike.
	 * Registered tracks survive the reload. Use after editing the config externally (e.g. from
	 * the web dashboard).
	 */
	void reloadConfig();

	/**
	 * Awards a milestone to an online player right now, whether or not they have reached it and
	 * whether or not they were paid for it before. The player's recorded progress is left alone,
	 * so this hands out the rewards a second time if they already earned them.
	 *
	 * @param player      the player to award
	 * @param milestoneId the milestone id
	 * @return {@code true} if the milestone was awarded, {@code false} if the id is unknown or a
	 * listener cancelled {@link dev.drawethree.xprison.api.milestones.events.PlayerMilestoneReachedEvent}
	 */
	boolean awardMilestone(@NotNull Player player, @NotNull String milestoneId);
}
