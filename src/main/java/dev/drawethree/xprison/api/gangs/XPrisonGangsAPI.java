package dev.drawethree.xprison.api.gangs;

import dev.drawethree.xprison.api.gangs.enums.GangCreateResult;
import dev.drawethree.xprison.api.gangs.enums.GangNameCheckResult;
import dev.drawethree.xprison.api.gangs.model.Gang;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

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
}
