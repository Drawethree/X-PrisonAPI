package dev.drawethree.xprison.api.mines;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.mines.model.MineSelection;
import org.bukkit.Location;

/**
 * API for interacting with mines in the XPrison plugin.
 */
public interface XPrisonMinesAPI {

	/**
	 * Gets a mine by its name.
	 *
	 * @param name The name of the mine
	 * @return The {@link Mine} instance matching the given name, or null if no such mine exists
	 */
	Mine getMineByName(String name);

	/**
	 * Gets the mine located at a specific location.
	 *
	 * @param loc The {@link Location} to check
	 * @return The {@link Mine} containing the given location, or null if none is found
	 */
	Mine getMineAtLocation(Location loc);

	/**
	 * Creates a new mine with the specified selection boundaries and name.
	 *
	 * @param mineSelection The {@link MineSelection} defining the mine boundaries
	 * @param name          The name of the new mine
	 * @return The created {@link Mine} instance
	 */
	Mine createMine(MineSelection mineSelection, String name);

	/**
	 * Deletes the specified mine.
	 *
	 * @param mine The {@link Mine} to delete
	 * @return true if the mine was successfully deleted, false otherwise
	 */
	boolean deleteMine(Mine mine);

	/**
	 * Resets the contents of the given mine.
	 *
	 * @param mine The {@link Mine} to reset
	 */
	void resetMine(Mine mine);
}
