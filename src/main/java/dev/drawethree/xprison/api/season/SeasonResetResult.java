package dev.drawethree.xprison.api.season;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The outcome of one {@link SeasonResetParticipant} during a season reset.
 *
 * @since 1.10
 */
public final class SeasonResetResult {

	/**
	 * How a participant's reset ended.
	 */
	public enum Status {
		/**
		 * The data was wiped.
		 */
		DONE,
		/**
		 * The participant chose not to reset; nothing was deleted.
		 */
		SKIPPED,
		/**
		 * The reset threw or reported an error; its data may be partly wiped.
		 */
		FAILED
	}

	private final Status status;
	private final long records;
	private final String message;

	private SeasonResetResult(Status status, long records, String message) {
		this.status = status;
		this.records = records;
		this.message = Objects.requireNonNull(message, "message");
	}

	/**
	 * The data was wiped.
	 *
	 * @param records how many records were removed, or {@code -1} if unknown
	 * @return the result
	 */
	@NotNull
	public static SeasonResetResult done(long records) {
		return new SeasonResetResult(Status.DONE, records, "");
	}

	/**
	 * Nothing was deleted because it was not safe to reset now.
	 *
	 * @param reason shown to the server owner
	 * @return the result
	 */
	@NotNull
	public static SeasonResetResult skipped(@NotNull String reason) {
		return new SeasonResetResult(Status.SKIPPED, 0, reason);
	}

	/**
	 * The reset failed.
	 *
	 * @param reason shown to the server owner
	 * @return the result
	 */
	@NotNull
	public static SeasonResetResult failed(@NotNull String reason) {
		return new SeasonResetResult(Status.FAILED, -1, reason);
	}

	/**
	 * Gets how the reset ended.
	 *
	 * @return the status
	 */
	@NotNull
	public Status getStatus() {
		return this.status;
	}

	/**
	 * Gets how many records were removed.
	 *
	 * @return the record count, or {@code -1} if unknown
	 */
	public long getRecords() {
		return this.records;
	}

	/**
	 * Gets the reason for a skip or failure.
	 *
	 * @return the message, empty when the reset succeeded
	 */
	@NotNull
	public String getMessage() {
		return this.message;
	}
}
