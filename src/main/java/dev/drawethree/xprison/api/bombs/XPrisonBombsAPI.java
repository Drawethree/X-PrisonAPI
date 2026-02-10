package dev.drawethree.xprison.api.bombs;

import dev.drawethree.xprison.api.bombs.model.Bomb;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * The {@code XPrisonBombsAPI} interface provides methods for managing and interacting with
 * bomb items within the XPrison plugin ecosystem. This includes giving bombs to players
 * and retrieving bomb definitions by name.
 */
public interface XPrisonBombsAPI {

    /**
     * Gives a specified amount of a {@link Bomb} to a {@link Player}.
     *
     * @param bomb   the bomb instance to give to the player
     * @param amount the number of bombs to give
     * @param player the player who will receive the bomb(s)
     */
    void giveBomb(Bomb bomb, int amount, Player player);

    /**
     * Retrieves a {@link Bomb} by its name.
     *
     * @param name the name of the bomb to retrieve
     * @return an {@link Optional} containing the found bomb, or empty if not found
     */
    Optional<Bomb> getBombByName(String name);

	/**
	 * Retrieves all registered {@link Bomb} instances within the XPrison plugin.
	 *
	 * <p>This method returns a collection of all bombs that are currently defined
	 * and available for use, regardless of whether they have been given to any
	 * player or not.</p>
	 *
	 * @return a {@link Collection} containing all registered {@link Bomb} instances;
	 *         the collection will be empty if no bombs are registered
	 */
	Collection<Bomb> getAllBombs();

}
