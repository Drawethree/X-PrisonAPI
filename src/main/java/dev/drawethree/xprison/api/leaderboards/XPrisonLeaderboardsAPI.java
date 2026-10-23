package dev.drawethree.xprison.api.leaderboards;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

/**
 * Access to the Leaderboards module: the boards behind {@code /top}, the leaderboard holograms and
 * the {@code %xprison_lb_...%} placeholders.
 * <p>
 * Every read on this interface is served from a cached snapshot that X-Prison refreshes on the
 * interval set in {@code leaderboards.yml}, so all methods are cheap and safe to call from the main
 * thread.
 *
 * @since 1.10
 */
public interface XPrisonLeaderboardsAPI {

	/**
	 * Registers a board. Its first snapshot is computed asynchronously right away.
	 *
	 * @param source the board to register
	 * @return {@code true} if it was registered, {@code false} if a board with the same id exists
	 */
	boolean registerSource(@NotNull LeaderboardSource source);

	/**
	 * Removes a board along with its cached snapshot. Holograms placed for it stay saved and show
	 * their empty-board lines until a board with the same id registers again.
	 *
	 * @param boardId the id of the board to remove
	 * @return {@code true} if a board was removed
	 */
	boolean unregisterSource(@NotNull String boardId);

	/**
	 * Gets the ids of every registered board that is enabled in {@code leaderboards.yml}, in display
	 * order.
	 *
	 * @return the board ids
	 */
	@NotNull
	List<String> getBoardIds();

	/**
	 * Gets the cached top of a board.
	 *
	 * @param boardId the board id
	 * @param limit   the maximum number of entries to return
	 * @return the entries, best first; empty if the board is unknown or has no data yet
	 */
	@NotNull
	List<LeaderboardEntry> getTop(@NotNull String boardId, int limit);

	/**
	 * Gets the cached entry at a position of a board.
	 *
	 * @param boardId  the board id
	 * @param position the 1-based position
	 * @return the entry, or {@code null} if the position is empty or outside the cached snapshot
	 */
	@Nullable
	LeaderboardEntry getEntry(@NotNull String boardId, int position);

	/**
	 * Gets a player's position on a board.
	 *
	 * @param boardId the board id
	 * @param uuid    the player
	 * @return the 1-based position, or empty if the player is not within the cached snapshot
	 */
	@NotNull
	OptionalInt getPosition(@NotNull String boardId, @NotNull UUID uuid);
}
