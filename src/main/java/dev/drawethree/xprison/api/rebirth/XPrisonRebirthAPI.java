package dev.drawethree.xprison.api.rebirth;

import dev.drawethree.xprison.api.rebirth.model.Rebirth;
import dev.drawethree.xprison.api.shared.Pagination;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * API interface for interacting with the XPrison Rebirth system.
 */
public interface XPrisonRebirthAPI {

	/**
	 * Gets a rebirth by its unique ID.
	 *
	 * @param id the ID of the rebirth
	 * @return the Rebirth corresponding to the given ID
	 */
	Rebirth getRebirthById(int id);

	/**
	 * Gets the current rebirth of a player.
	 *
	 * @param player the player whose rebirth is requested
	 * @return the player's current Rebirth
	 */
	Rebirth getPlayerRebirth(Player player);

	/**
	 * Gets the next rebirth a player can achieve.
	 *
	 * @param player the player whose next rebirth is requested
	 * @return an Optional containing the next Rebirth, or empty if the player has the max rebirth
	 */
	Optional<Rebirth> getNextPlayerRebirth(Player player);

	/**
	 * Sets a player's rebirth to the specified Rebirth.
	 *
	 * @param player  the player whose rebirth is to be set
	 * @param rebirth the Rebirth to assign to the player
	 */
	void setPlayerRebirth(Player player, Rebirth rebirth);

	/**
	 * Resets a player's rebirth to the default rebirth (usually 0 or 1 depending on config).
	 *
	 * @param player the player whose rebirth will be reset
	 */
	void resetPlayerRebirth(Player player);

	/**
	 * Checks if a player currently holds the maximum possible rebirth.
	 *
	 * @param player the player to check
	 * @return true if the player has the max rebirth, false otherwise
	 */
	boolean isMaxRebirth(Player player);

	/**
	 * Attempts to rebirth a player, checking all requirements.
	 *
	 * @param player the player attempting to rebirth
	 * @return true if the rebirth was successful, false otherwise
	 */
	boolean tryRebirth(Player player);

	/**
	 * Gets the current rebirth of a player by UUID, including offline players.
	 * Reads from the in-memory cache if online, otherwise queries the database.
	 *
	 * @param playerUuid the UUID of the player
	 * @return the player's current Rebirth, or null if not found
	 */
	Rebirth getPlayerRebirthOffline(UUID playerUuid);

	/**
	 * Sets the rebirth of an offline player (database-only update; takes effect on next login).
	 *
	 * @param playerUuid the UUID of the player
	 * @param rebirthId  the rebirth ID to assign
	 */
	void setPlayerRebirthOffline(UUID playerUuid, long rebirthId);

	/**
	 * Returns the top N players by rebirth ID, ordered descending.
	 *
	 * @param limit maximum number of entries to return
	 * @return ordered map of UUID → rebirth ID
	 */
	Map<UUID, Integer> getTopByRebirth(int limit);

	/**
	 * Returns all configured rebirths in ascending order.
	 *
	 * @return ordered list of all defined rebirths
	 */
	@NotNull
	List<Rebirth> getAllRebirths();

	/**
	 * Returns the highest configured rebirth, or {@code null} if none are defined.
	 *
	 * @return the max rebirth
	 */
	@Nullable
	Rebirth getMaxRebirth();

	/**
	 * Returns all player UUIDs stored in the rebirths database.
	 *
	 * @return list of all UUIDs that have a row in the rebirths table
	 */
	@NotNull
	List<UUID> getAllPlayerUUIDs();

	/**
	 * Paginated variant of {@link #getTopByRebirth(int)}.
	 * <p>
	 * The default implementation over-fetches and slices the ordered result client-side.
	 *
	 * @param limit  maximum number of entries to return
	 * @param offset number of leading entries to skip (0 = start from top)
	 * @return ordered map of UUID → rebirth ID
	 */
	@NotNull
	default Map<UUID, Integer> getTopByRebirth(int limit, int offset) {
		return Pagination.slice(getTopByRebirth(limit + Math.max(offset, 0)), limit, offset);
	}

	/**
	 * Asynchronous variant of {@link #getTopByRebirth(int)} that runs the query off the server thread.
	 *
	 * @param limit maximum number of entries to return
	 * @return a future completing with the ordered map of UUID → rebirth ID
	 */
	@NotNull
	default CompletableFuture<Map<UUID, Integer>> getTopByRebirthAsync(int limit) {
		return CompletableFuture.supplyAsync(() -> getTopByRebirth(limit));
	}

	/**
	 * Asynchronous variant of {@link #getPlayerRebirthOffline(UUID)} that runs the query off the server thread.
	 *
	 * @param playerUuid the UUID of the player
	 * @return a future completing with the player's rebirth, or {@code null} if not found
	 */
	@NotNull
	default CompletableFuture<Rebirth> getPlayerRebirthOfflineAsync(UUID playerUuid) {
		return CompletableFuture.supplyAsync(() -> getPlayerRebirthOffline(playerUuid));
	}
}
