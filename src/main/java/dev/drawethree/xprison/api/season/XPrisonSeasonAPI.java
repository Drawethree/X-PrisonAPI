package dev.drawethree.xprison.api.season;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Access to the season reset: register the data your plugin or addon wants wiped when the server
 * starts a new season.
 *
 * @since 1.10
 */
public interface XPrisonSeasonAPI {

	/**
	 * Registers a participant so it appears in the Season Reset menu and {@code /xprison season}.
	 *
	 * @param participant the participant
	 * @return {@code true} if it was registered, {@code false} if one with the same id exists
	 */
	boolean registerParticipant(@NotNull SeasonResetParticipant participant);

	/**
	 * Removes a participant, for example when your addon disables.
	 *
	 * @param participantId the participant id
	 * @return {@code true} if one was removed
	 */
	boolean unregisterParticipant(@NotNull String participantId);

	/**
	 * Gets every registered participant in the order a reset would run them.
	 *
	 * @return the participants
	 */
	@NotNull
	List<SeasonResetParticipant> getParticipants();

	/**
	 * Whether a season reset is running right now.
	 *
	 * @return {@code true} while a reset is in progress
	 */
	boolean isResetRunning();
}
