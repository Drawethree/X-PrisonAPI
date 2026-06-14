package dev.drawethree.xprison.api.prestiges;

import dev.drawethree.xprison.api.prestiges.model.Prestige;
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
 * API interface for handling prestige-related operations in XPrison.
 */
public interface XPrisonPrestigesAPI {

	/**
	 * Retrieves a Prestige by its unique ID.
	 *
	 * @param id the Prestige ID
	 * @return the Prestige instance matching the given ID
	 */
	Prestige getPrestigeById(long id);

	/**
	 * Gets the current Prestige of a player.
	 *
	 * @param p the player whose prestige is to be retrieved
	 * @return the Prestige instance of the player
	 */
	Prestige getPlayerPrestige(Player p);

	/**
	 * Sets the Prestige level of an online player.
	 *
	 * @param player   the player whose prestige is to be set
	 * @param prestige the Prestige instance to assign
	 */
	void setPlayerPrestige(Player player, Prestige prestige);

	/**
	 * Sets the Prestige level of an online player by prestige ID.
	 *
	 * @param player     the player whose prestige is to be set
	 * @param prestigeId the ID of the prestige level to assign
	 */
	void setPlayerPrestige(Player player, long prestigeId);

    /**
     * Gets the player's progress towards prestiging up, as a percentage.
     *
     * @param player the player whose prestige progress is requested
     * @return double between 0.0 and 100.0 representing the percentage of prestige progress
     */
    double getPrestigeProgress(Player player);

	/**
	 * Checks if a player has reached the maximum prestige level.
	 *
	 * @param player the player to check
	 * @return true if the player is at max prestige, false otherwise
	 */
	boolean isMaxPrestige(Player player);

	/**
	 * Resets a player's prestige to 0.
	 *
	 * @param player the player whose prestige will be reset
	 */
	void resetPlayerPrestige(Player player);

	/**
	 * Gets the current prestige of a player by UUID, including offline players.
	 * Reads from the in-memory cache if online, otherwise queries the database.
	 *
	 * @param playerUuid the UUID of the player
	 * @return the player's current Prestige, or null if not found
	 */
	Prestige getPlayerPrestigeOffline(UUID playerUuid);

	/**
	 * Sets the prestige of an offline player (database-only update; takes effect on next login).
	 *
	 * @param playerUuid the UUID of the player
	 * @param prestigeId the prestige ID to assign
	 */
	void setPlayerPrestigeOffline(UUID playerUuid, long prestigeId);

	/**
	 * Returns the top N players by prestige, ordered descending.
	 *
	 * @param limit maximum number of entries to return
	 * @return ordered map of UUID → prestige ID
	 */
	Map<UUID, Long> getTopByPrestige(int limit);

	/**
	 * Returns all configured prestiges in ascending order.
	 * For servers using unlimited prestiges, returns an empty list.
	 *
	 * @return ordered list of all defined prestiges
	 */
	@NotNull
	List<Prestige> getAllPrestiges();

	/**
	 * Returns the maximum prestige configured on this server.
	 * Returns {@code null} on servers using unlimited prestiges.
	 *
	 * @return the max prestige, or {@code null} if not applicable
	 */
	@Nullable
	Prestige getMaxPrestige();

	/**
	 * Gets the next prestige a player can achieve.
	 *
	 * @param player the player whose next prestige is requested
	 * @return an Optional containing the next Prestige, or empty if the player has the max prestige
	 */
	Optional<Prestige> getNextPlayerPrestige(Player player);

	/**
	 * Returns all player UUIDs stored in the prestiges database.
	 *
	 * @return list of all UUIDs that have a row in the prestiges table
	 */
	@NotNull
	List<UUID> getAllPlayerUUIDs();

	/**
	 * Paginated variant of {@link #getTopByPrestige(int)}.
	 * <p>
	 * The default implementation over-fetches and slices the ordered result client-side.
	 *
	 * @param limit  maximum number of entries to return
	 * @param offset number of leading entries to skip (0 = start from top)
	 * @return ordered map of UUID → prestige ID
	 */
	@NotNull
	default Map<UUID, Long> getTopByPrestige(int limit, int offset) {
		return Pagination.slice(getTopByPrestige(limit + Math.max(offset, 0)), limit, offset);
	}

	/**
	 * Asynchronous variant of {@link #getTopByPrestige(int)} that runs the query off the server thread.
	 *
	 * @param limit maximum number of entries to return
	 * @return a future completing with the ordered map of UUID → prestige ID
	 */
	@NotNull
	default CompletableFuture<Map<UUID, Long>> getTopByPrestigeAsync(int limit) {
		return CompletableFuture.supplyAsync(() -> getTopByPrestige(limit));
	}

	/**
	 * Asynchronous variant of {@link #getPlayerPrestigeOffline(UUID)} that runs the query off the server thread.
	 *
	 * @param playerUuid the UUID of the player
	 * @return a future completing with the player's prestige, or {@code null} if not found
	 */
	@NotNull
	default CompletableFuture<Prestige> getPlayerPrestigeOfflineAsync(UUID playerUuid) {
		return CompletableFuture.supplyAsync(() -> getPlayerPrestigeOffline(playerUuid));
	}
}
