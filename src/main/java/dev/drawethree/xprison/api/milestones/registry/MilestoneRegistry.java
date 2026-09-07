package dev.drawethree.xprison.api.milestones.registry;

import dev.drawethree.xprison.api.milestones.model.Milestone;
import dev.drawethree.xprison.api.milestones.model.MilestoneTrack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * What milestones exist, and which tracks they can be built on.
 * <p>
 * The ladders themselves come from {@code milestones.yml} and are read-only here; the tracks are
 * the open half - register one and server owners can immediately build a ladder on it.
 *
 * @since 1.9
 */
public interface MilestoneRegistry {

	/**
	 * Registers a track, making {@code type: <key>} usable in {@code milestones.yml}. Any
	 * configured milestone that named this track before it existed is adopted straight away, so a
	 * plugin may register long after the config was read.
	 *
	 * @param track the track to register
	 * @return {@code true} if it was registered, {@code false} if another track already holds
	 * that key
	 */
	boolean registerTrack(@NotNull MilestoneTrack track);

	/**
	 * Removes a track. Its milestones stay in the config but stop being measured until a track
	 * with the same key registers again. X-Prison's own tracks cannot be removed.
	 *
	 * @param trackKey the key of the track to remove
	 * @return {@code true} if a track was removed
	 */
	boolean unregisterTrack(@NotNull String trackKey);

	/**
	 * Gets every registered track, in registration order - X-Prison's own first.
	 *
	 * @return the registered tracks
	 */
	@NotNull
	List<MilestoneTrack> getTracks();

	/**
	 * Gets the tracks that have at least one milestone configured, which is what the menu shows.
	 *
	 * @return the tracks in use
	 */
	@NotNull
	List<MilestoneTrack> getConfiguredTracks();

	/**
	 * Looks a track up by its config key, ignoring case.
	 *
	 * @param trackKey the key to resolve
	 * @return the track, or {@code null} if no track holds that key
	 */
	@Nullable
	MilestoneTrack getTrack(@NotNull String trackKey);

	/**
	 * Gets every configured milestone across all tracks.
	 *
	 * @return the milestones, possibly empty
	 */
	@NotNull
	List<Milestone> getMilestones();

	/**
	 * Gets the ladder of one track, ordered from the lowest threshold upwards.
	 *
	 * @param track the track to read
	 * @return the track's milestones, possibly empty
	 */
	@NotNull
	List<Milestone> getMilestones(@NotNull MilestoneTrack track);

	/**
	 * Looks a milestone up by its config key.
	 *
	 * @param milestoneId the milestone id
	 * @return the milestone, or {@code null} if no milestone has that id
	 */
	@Nullable
	Milestone getMilestone(@NotNull String milestoneId);
}
