package dev.drawethree.xprison.api.bombs;

import dev.drawethree.xprison.api.bombs.model.Bomb;
import org.bukkit.entity.Player;

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

}
