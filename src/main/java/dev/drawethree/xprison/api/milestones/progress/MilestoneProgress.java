package dev.drawethree.xprison.api.milestones.progress;

import dev.drawethree.xprison.api.milestones.model.Milestone;
import dev.drawethree.xprison.api.milestones.model.MilestoneTrack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Where a player stands on the ladders.
 * <p>
 * Two numbers matter per track: the player's live value, which a prestige or rebirth reset takes
 * back down, and the highest value they have ever held, which does not move. The second is what
 * stops a milestone paying out twice, so it is also the one to edit when a milestone should be
 * earnable again.
 *
 * @since 1.9
 */
public interface MilestoneProgress {

	/**
	 * Gets the player's live value on a track.
	 *
	 * @param player the player to read
	 * @param track  the track to read
	 * @return the current value, or {@link BigDecimal#ZERO} if the track cannot be measured
	 */
	@NotNull
	BigDecimal getCurrentValue(@NotNull Player player, @NotNull MilestoneTrack track);

	/**
	 * Gets how many milestones of a track the player has reached, counted from the highest value
	 * they have ever held rather than their current one.
	 *
	 * @param player the player to read
	 * @param track  the track to read
	 * @return the number of milestones reached on that track
	 */
	int getLevel(@NotNull Player player, @NotNull MilestoneTrack track);

	/**
	 * Gets the next milestone the player has not reached yet on a track.
	 *
	 * @param player the player to read
	 * @param track  the track to read
	 * @return the next milestone, or {@code null} if the whole ladder is finished
	 */
	@Nullable
	Milestone getNextMilestone(@NotNull Player player, @NotNull MilestoneTrack track);

	/**
	 * Gets the highest value the player has ever recorded on a track. This is the mark that stops
	 * a milestone paying out twice, and it is not lowered by a prestige or rebirth reset. Works
	 * for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param track      the track to read
	 * @return the recorded high-water mark, or {@link BigDecimal#ZERO} if the player has none
	 */
	@NotNull
	BigDecimal getHighestProgress(@NotNull UUID playerUuid, @NotNull MilestoneTrack track);

	/**
	 * Sets a player's high-water mark on a track (clamped to {@code >= 0}), which decides what
	 * they may still be paid for. Lowering it lets already-reached milestones pay out again.
	 * Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param track      the track to write
	 * @param value      the high-water mark to store
	 */
	void setHighestProgress(@NotNull UUID playerUuid, @NotNull MilestoneTrack track, @NotNull BigDecimal value);

	/**
	 * Convenience form of
	 * {@link #setHighestProgress(UUID, MilestoneTrack, BigDecimal)} for tracks that count in whole
	 * numbers.
	 *
	 * @param playerUuid the player's unique id
	 * @param track      the track to write
	 * @param value      the high-water mark to store
	 */
	default void setHighestProgress(@NotNull UUID playerUuid, @NotNull MilestoneTrack track, long value) {
		setHighestProgress(playerUuid, track, BigDecimal.valueOf(value));
	}

	/**
	 * Clears a player's recorded progress on every track, so the whole ladder can be earned
	 * again. Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 */
	void resetPlayer(@NotNull UUID playerUuid);

	// ---------------------------------------------------------------------
	// Driving a track (a track only pays out when its movement is reported)
	// ---------------------------------------------------------------------

	/**
	 * Reports a track's value after it changed, for a statistic that can go back down - a
	 * prestige, a rebirth, a rank. Milestones that match the value exactly fire when it lands on
	 * them; milestones that are not exact fire at or past their threshold.
	 * <p>
	 * X-Prison drives its own tracks; a plugin that registered a track calls this (or one of the
	 * two below) whenever its statistic moves, otherwise the ladder is drawn in the menu but
	 * never pays out.
	 *
	 * @param player the player whose value changed
	 * @param track  the track that moved
	 * @param value  the new value
	 */
	void reportValue(@NotNull Player player, @NotNull MilestoneTrack track, @NotNull BigDecimal value);

	/**
	 * Convenience form of {@link #reportValue(Player, MilestoneTrack, BigDecimal)}.
	 *
	 * @param player the player whose value changed
	 * @param track  the track that moved
	 * @param value  the new value
	 */
	default void reportValue(@NotNull Player player, @NotNull MilestoneTrack track, long value) {
		reportValue(player, track, BigDecimal.valueOf(value));
	}

	/**
	 * Reports a jump on a counter that only ever climbs - blocks broken, minutes played, a
	 * lifetime total. Every threshold the jump crossed fires once, in ascending order, and a
	 * threshold already crossed never fires again.
	 *
	 * @param player   the player whose counter moved
	 * @param track    the track that moved
	 * @param previous the value before the jump
	 * @param current  the value after it
	 */
	void reportCounter(@NotNull Player player, @NotNull MilestoneTrack track,
	                   @NotNull BigDecimal previous, @NotNull BigDecimal current);

	/**
	 * Convenience form of {@link #reportCounter(Player, MilestoneTrack, BigDecimal, BigDecimal)}.
	 *
	 * @param player   the player whose counter moved
	 * @param track    the track that moved
	 * @param previous the value before the jump
	 * @param current  the value after it
	 */
	default void reportCounter(@NotNull Player player, @NotNull MilestoneTrack track,
	                           long previous, long current) {
		reportCounter(player, track, BigDecimal.valueOf(previous), BigDecimal.valueOf(current));
	}

	/**
	 * Reports a jump on a statistic that can also go back down, which is how a multi-level
	 * prestige is paid: every threshold between the two values fires once, and milestones that
	 * are not exact fire again on every further step.
	 *
	 * @param player   the player whose value changed
	 * @param track    the track that moved
	 * @param previous the value before the jump
	 * @param current  the value after it
	 */
	void reportChange(@NotNull Player player, @NotNull MilestoneTrack track,
	                  @NotNull BigDecimal previous, @NotNull BigDecimal current);

	/**
	 * Convenience form of {@link #reportChange(Player, MilestoneTrack, BigDecimal, BigDecimal)}.
	 *
	 * @param player   the player whose value changed
	 * @param track    the track that moved
	 * @param previous the value before the jump
	 * @param current  the value after it
	 */
	default void reportChange(@NotNull Player player, @NotNull MilestoneTrack track,
	                          long previous, long current) {
		reportChange(player, track, BigDecimal.valueOf(previous), BigDecimal.valueOf(current));
	}
}
