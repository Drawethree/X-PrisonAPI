package dev.drawethree.xprison.api.prestiges;

import dev.drawethree.xprison.api.prestiges.model.Prestige;
import org.bukkit.entity.Player;

public interface XPrisonPrestigesAPI {

	/**
	 * Method to get  Prestige by id
	 *
	 * @param id Prestige id
	 * @return Prestige
	 */
	Prestige getPrestigeById(long id);

	/**
	 * Method to get player Prestige
	 *
	 * @param p Player
	 * @return Prestige
	 */
	Prestige getPlayerPrestige(Player p);

	/**
	 * Sets a prestige to online player
	 *
	 * @param player   Player
	 * @param prestige Prestige
	 */
	void setPlayerPrestige(Player player, Prestige prestige);

	/**
	 * Sets a prestige to online player
	 *
	 * @param player   Player
	 * @param prestigeId Prestige level
	 */
	void setPlayerPrestige(Player player, long prestigeId);


	/**
	 * Returns true if a player is on max prestige, otherwise false
	 *
	 * @param player   Player
	 */
	boolean isMaxPrestige(Player player);


}
