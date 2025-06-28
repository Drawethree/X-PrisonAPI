package dev.drawethree.xprison.api.prestiges;

import dev.drawethree.xprison.api.prestiges.model.Prestige;
import org.bukkit.entity.Player;

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
	 * Checks if a player has reached the maximum prestige level.
	 *
	 * @param player the player to check
	 * @return true if the player is at max prestige, false otherwise
	 */
	boolean isMaxPrestige(Player player);

}
