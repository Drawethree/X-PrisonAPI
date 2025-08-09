package dev.drawethree.xprison.api.rebirth;

import dev.drawethree.xprison.api.rebirth.model.Rebirth;
import org.bukkit.entity.Player;

import java.util.Optional;

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

}
