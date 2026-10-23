package dev.drawethree.xprison.api.leaderboards;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Supplies the data behind one leaderboard.
 * <p>
 * Register an implementation with {@link XPrisonLeaderboardsAPI#registerSource(LeaderboardSource)}
 * and the board immediately shows up in {@code /top}, can be placed as a hologram and gets its own
 * {@code %xprison_lb_<id>_...%} placeholders. X-Prison caches the result and calls
 * {@link #fetch(int)} periodically, so implementations may query a database directly.
 *
 * @since 1.10
 */
public interface LeaderboardSource {

	/**
	 * Gets the unique, stable id of this board. It is used in commands, placeholders, hologram
	 * placements and {@code leaderboards.yml}, so it must not change between restarts. Use lower
	 * case letters, digits and underscores, for example {@code "pets_level"}.
	 *
	 * @return the board id
	 */
	@NotNull
	String getId();

	/**
	 * Gets the default display name of this board. Server owners can override it in
	 * {@code leaderboards.yml}.
	 *
	 * @return the display name, MiniMessage allowed
	 */
	@NotNull
	String getDisplayName();

	/**
	 * Computes the current top of this board, best first.
	 * <p>
	 * Always called off the main server thread. Return at most {@code limit} entries; returning
	 * fewer is fine. Exceptions are caught and logged, and the previous result is kept.
	 *
	 * @param limit the maximum number of entries to return
	 * @return the ranked entries, best first
	 */
	@NotNull
	List<LeaderboardEntry> fetch(int limit);
}
