package dev.drawethree.xprison.api.milestones;

import dev.drawethree.xprison.api.milestones.model.Milestone;
import dev.drawethree.xprison.api.milestones.model.MilestoneType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * API interface for the Milestones module.
 * <p>
 * Milestones are long ladders of goals across independent tracks - prestige, rebirth, blocks
 * broken, playtime and so on. Each rung pays out the first time a player reaches it and never
 * again, which is tracked as a per-track high-water mark that survives a prestige or rebirth
 * reset.
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
	 * Gets every track that has at least one milestone configured, in the order the ladders were
	 * read from the config.
	 *
	 * @return the configured tracks, possibly empty
	 */
	@NotNull
	List<MilestoneType> getTracks();

	/**
	 * Gets every configured milestone across all tracks.
	 *
	 * @return an unmodifiable list of milestones, possibly empty
	 */
	@NotNull
	List<Milestone> getMilestones();

	/**
	 * Gets the ladder of one track, ordered from the lowest threshold upwards.
	 *
	 * @param type the track to read
	 * @return the track's milestones, possibly empty
	 */
	@NotNull
	List<Milestone> getMilestones(@NotNull MilestoneType type);

	/**
	 * Looks a milestone up by its config key.
	 *
	 * @param milestoneId the milestone id
	 * @return the milestone, or {@code null} if no milestone has that id
	 */
	@Nullable
	Milestone getMilestone(@NotNull String milestoneId);

	/**
	 * Gets the player's live value on a track - their prestige id, broken-block total, minutes
	 * played and so on.
	 *
	 * @param player the player to read
	 * @param type   the track to read
	 * @return the current value, or {@code 0} if the track's module is unavailable
	 */
	long getCurrentValue(@NotNull Player player, @NotNull MilestoneType type);

	/**
	 * Gets how many milestones of a track the player has reached, counting from the highest value
	 * they have ever held rather than their current one.
	 *
	 * @param player the player to read
	 * @param type   the track to read
	 * @return the number of milestones reached on that track
	 */
	int getLevel(@NotNull Player player, @NotNull MilestoneType type);

	/**
	 * Gets the next milestone the player has not reached yet on a track.
	 *
	 * @param player the player to read
	 * @param type   the track to read
	 * @return the next milestone, or {@code null} if the whole ladder is finished
	 */
	@Nullable
	Milestone getNextMilestone(@NotNull Player player, @NotNull MilestoneType type);

	/**
	 * Gets the highest value the player has ever recorded on a track. This is the mark that stops
	 * a milestone paying out twice, and it is not lowered by a prestige or rebirth reset. Works
	 * for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param type       the track to read
	 * @return the recorded high-water mark, or {@code 0} if the player has none
	 */
	long getHighestProgress(@NotNull UUID playerUuid, @NotNull MilestoneType type);

	// ---------------------------------------------------------------------
	// Administration (config + offline-capable player data)
	// ---------------------------------------------------------------------

	/**
	 * Reloads the Milestones configuration from {@code milestones.yml}, ladders and menu alike.
	 * Use after editing the config externally (e.g. from the web dashboard).
	 */
	void reloadConfig();

	/**
	 * Sets a player's high-water mark on a track (clamped to {@code >= 0}), which decides what
	 * they may still be paid for. Lowering it lets already-reached milestones pay out again.
	 * Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param type       the track to write
	 * @param value      the high-water mark to store
	 */
	void setHighestProgress(@NotNull UUID playerUuid, @NotNull MilestoneType type, long value);

	/**
	 * Clears a player's recorded progress on every track, so the whole ladder can be earned
	 * again. Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 */
	void resetPlayer(@NotNull UUID playerUuid);

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
