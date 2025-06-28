package dev.drawethree.xprison.api.gangs;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.gangs.enums.GangCreateResult;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public interface XPrisonGangsAPI {

	/**
	 * Method to get Gang from player
	 *
	 * @param player OfflinePlayer
	 * @return Optional<Gang> gang
	 */
	Optional<Gang> getPlayerGang(OfflinePlayer player);

	/**
	 * Method to get Gang from name
	 *
	 * @param name name of gang
	 * @return Optional<Gang> gang
	 */
	Optional<Gang> getByName(String name);

	/**
	 * Method to get all gangs
	 *
	 * @return List of gangs
	 */
	Collection<Gang> getAllGangs();

	/**
	 * Creates a gang with given name and specific gangleader
	 *
	 * @return result of gang creation
	 */
	GangCreateResult createGang(String name, Player gangLeader);

	/**
	 * Disbands a gang, if exists
	 */
	void disbandGang(Gang gang);





}
