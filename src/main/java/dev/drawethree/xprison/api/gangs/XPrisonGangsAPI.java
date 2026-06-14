package dev.drawethree.xprison.api.gangs;

import dev.drawethree.xprison.api.gangs.enums.GangCreateResult;
import dev.drawethree.xprison.api.gangs.enums.GangNameCheckResult;
import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.Pagination;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * API for interacting with the Gangs system in the XPrison plugin.
 * Provides methods to retrieve, create, disband, and validate gangs, as well as access gang membership information.
 */
public interface XPrisonGangsAPI {

	/**
	 * Retrieves the gang associated with the specified player, if any.
	 *
	 * @param player the {@link OfflinePlayer} whose gang membership is to be queried
	 * @return an {@link Optional} containing the player's {@link Gang}, or empty if the player is not in a gang
	 */
	Optional<Gang> getPlayerGang(OfflinePlayer player);

	/**
	 * Retrieves a gang by its name, if it exists.
	 *
	 * @param name the name of the gang
	 * @return an {@link Optional} containing the {@link Gang}, or empty if no gang with that name exists
	 */
	Optional<Gang> getByName(String name);

	/**
	 * Returns a collection of all registered gangs.
	 *
	 * @return a {@link Collection} of all {@link Gang}s
	 */
	Collection<Gang> getAllGangs();

	/**
	 * Attempts to create a new gang with the specified name and leader.
	 *
	 * @param name       the desired name of the gang
	 * @param gangLeader the {@link Player} who will become the leader of the gang
	 * @return a {@link GangCreateResult} indicating the result of the creation attempt
	 */
	GangCreateResult createGang(String name, Player gangLeader);

	/**
	 * Disbands the specified gang if it exists.
	 * All gang members will be removed and the gang will be deleted.
	 *
	 * @param gang the {@link Gang} to disband
	 */
	void disbandGang(Gang gang);

	/**
	 * Checks whether a given gang name is valid and can be used.
	 *
	 * @param name the proposed gang name
	 * @return a {@link GangNameCheckResult} indicating if the name is valid or why it is not
	 */
	GangNameCheckResult checkGangName(String name);

	/**
	 * Returns the top N gangs by value, ordered descending.
	 *
	 * @param limit maximum number of entries to return
	 * @return ordered map of gang name → gang value
	 */
	LinkedHashMap<String, Long> getTopGangsByValue(int limit);

	/**
	 * Kicks a player from a gang.
	 * Has no effect if the player is not in the gang.
	 *
	 * @param gang   the gang to kick the player from
	 * @param player the player to kick
	 */
	void kickPlayerFromGang(Gang gang, OfflinePlayer player);

	/**
	 * Transfers gang ownership to a new owner.
	 * The new owner must already be a member of the gang.
	 *
	 * @param gang     the gang whose ownership is being transferred
	 * @param newOwner the player to become the new owner
	 * @return {@code true} if the transfer succeeded; {@code false} if the player is not a member
	 */
	boolean transferGangOwnership(Gang gang, OfflinePlayer newOwner);

	/**
	 * Sets the gang's value to a specific amount and persists the change.
	 *
	 * @param gang  the gang to update
	 * @param value the new value (must be ≥ 0)
	 */
	void setGangValue(Gang gang, long value);

	/**
	 * Adds to the gang's value and persists the change.
	 * Use a negative delta to subtract value.
	 *
	 * @param gang  the gang to update
	 * @param delta the amount to add (may be negative)
	 */
	void addGangValue(Gang gang, long delta);

	/**
	 * Paginated variant of {@link #getTopGangsByValue(int)}.
	 * <p>
	 * The default implementation over-fetches and slices the ordered result client-side.
	 *
	 * @param limit  maximum number of entries to return
	 * @param offset number of leading entries to skip (0 = start from top)
	 * @return ordered map of gang name → gang value
	 */
	@NotNull
	default LinkedHashMap<String, Long> getTopGangsByValue(int limit, int offset) {
		return new LinkedHashMap<>(Pagination.slice(getTopGangsByValue(limit + Math.max(offset, 0)), limit, offset));
	}

	/**
	 * Asynchronous variant of {@link #getTopGangsByValue(int)} that runs the query off the server thread.
	 *
	 * @param limit maximum number of entries to return
	 * @return a future completing with the ordered map of gang name → gang value
	 */
	@NotNull
	default CompletableFuture<LinkedHashMap<String, Long>> getTopGangsByValueAsync(int limit) {
		return CompletableFuture.supplyAsync(() -> getTopGangsByValue(limit));
	}
}
