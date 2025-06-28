package dev.drawethree.xprison.api.ranks;

import dev.drawethree.xprison.api.ranks.model.Rank;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface XPrisonRanksAPI {

	/**
	 * Gets a rank by its unique ID.
	 *
	 * @param id the ID of the rank
	 * @return the Rank corresponding to the given ID
	 */
	Rank getRankById(int id);

	/**
	 * Gets the current rank of a player.
	 *
	 * @param p the player whose rank is requested
	 * @return the player's current Rank
	 */
	Rank getPlayerRank(Player p);

	/**
	 * Gets the next rank a player can achieve.
	 *
	 * @param player the player whose next rank is requested
	 * @return an Optional containing the next Rank, or empty if the player has the max rank
	 */
	Optional<Rank> getNextPlayerRank(Player player);

	/**
	 * Gets the player's progress towards ranking up, as a percentage.
	 *
	 * @param player the player whose rankup progress is requested
	 * @return an integer between 0 and 100 representing the percentage of rankup progress
	 */
	int getRankupProgress(Player player);

	/**
	 * Sets a player's rank to the specified Rank.
	 *
	 * @param player the player whose rank is to be set
	 * @param rank   the Rank to assign to the player
	 */
	void setPlayerRank(Player player, Rank rank);

	/**
	 * Resets a player's rank to the default rank.
	 *
	 * @param player the player whose rank will be reset
	 */
	void resetPlayerRank(Player player);

	/**
	 * Checks if a player currently holds the maximum possible rank.
	 *
	 * @param player the player to check
	 * @return true if the player has the max rank, false otherwise
	 */
	boolean isMaxRank(Player player);
}
