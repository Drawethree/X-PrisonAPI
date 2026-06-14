package dev.drawethree.xprison.api.mines;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.mines.model.MineSelection;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collection;

/**
 * API for interacting with mines in the XPrison plugin.
 */
public interface XPrisonMinesAPI {


	/**
	 * Gets all mines
	 *
	 * @return The collection of {@link Mine}
	 */
	Collection<Mine> getMines();

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

	/**
	 * Sets the reset type for the given mine and persists the change.
	 *
	 * @param mineName  the mine name
	 * @param resetType "INSTANT" or "GRADUAL" (case-insensitive)
	 */
	void setMineResetType(String mineName, String resetType);

	/**
	 * Returns all mines located in the given world.
	 *
	 * @param world the world to filter by
	 * @return collection of mines in that world
	 */
	Collection<Mine> getMinesInWorld(World world);

	/**
	 * Renames a mine and persists the change.
	 *
	 * @param mine    the mine to rename
	 * @param newName the new name (must be non-blank and not already in use)
	 * @return {@code true} if the mine was renamed; {@code false} if the name was invalid or taken
	 */
	boolean renameMine(Mine mine, String newName);

	/**
	 * Sets the timed-reset interval for a mine and persists the change.
	 * <p>
	 * The interval is stored at whole-minute granularity, so the supplied seconds are rounded down to
	 * the nearest minute (minimum one minute).
	 *
	 * @param mine    the mine to update
	 * @param seconds the new reset interval, in seconds
	 */
	void setMineResetInterval(Mine mine, int seconds);
}
