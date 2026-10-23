package dev.drawethree.xprison.api.season;

import org.jetbrains.annotations.NotNull;

/**
 * Something whose data a season reset can wipe - a core module or an addon.
 * <p>
 * Register an implementation with {@link XPrisonSeasonAPI#registerParticipant(SeasonResetParticipant)}
 * and it appears in the Season Reset menu and in {@code /xprison season}, where the server owner
 * picks what to wipe. X-Prison drains pending database writes, runs the selected participants in
 * {@link #getResetPriority() priority} order, then reloads the data of anyone still online.
 * <p>
 * Every method except {@link #getId()}, {@link #getDisplayName()} and the flags is called off the
 * main server thread.
 *
 * @since 1.10
 */
public interface SeasonResetParticipant {

	/**
	 * Gets the unique, stable id of this participant, used by {@code /xprison season reset --include=}.
	 * Use lower case letters, digits, {@code _} and {@code -}, for example {@code "coinflip"}.
	 *
	 * @return the participant id
	 */
	@NotNull
	String getId();

	/**
	 * Gets the name shown in the Season Reset menu.
	 *
	 * @return the display name
	 */
	@NotNull
	String getDisplayName();

	/**
	 * Gets the order this participant runs in; lower runs first. Core currencies run at
	 * {@code 100}. A participant that holds currency on behalf of players (an open wager, an
	 * escrow) should run before that and give it back first.
	 *
	 * @return the priority, {@code 500} by default
	 */
	default int getResetPriority() {
		return 500;
	}

	/**
	 * Whether this participant is ticked when the Season Reset menu opens.
	 *
	 * @return {@code true} by default
	 */
	default boolean isSelectedByDefault() {
		return true;
	}

	/**
	 * Whether part of this participant's value lives on items in player inventories, which a reset
	 * cannot reach. The menu then tells the server owner to pair the reset with an inventory wipe.
	 *
	 * @return {@code false} by default
	 */
	default boolean holdsItemValue() {
		return false;
	}

	/**
	 * Counts the records a reset would remove, for the dry run.
	 *
	 * @return the number of records, or {@code -1} if it cannot be counted
	 */
	long countRecords();

	/**
	 * Wipes this participant's data.
	 * <p>
	 * Return {@link SeasonResetResult#skipped(String)} when it is not safe to reset right now (for
	 * example a game with money on the table is still running) - nothing is deleted then.
	 * Exceptions are caught and reported as a failure; the other participants still run.
	 *
	 * @return the outcome
	 */
	@NotNull
	SeasonResetResult reset();
}
