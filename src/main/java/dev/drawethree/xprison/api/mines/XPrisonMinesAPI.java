package dev.drawethree.xprison.api.mines;

import dev.drawethree.xprison.api.mines.model.Mine;
import dev.drawethree.xprison.api.mines.model.MineSelection;
import org.bukkit.Location;

public interface XPrisonMinesAPI {

	/**
	 * Gets a mine by name
	 *
	 * @param name String
	 * @return Mine.class
	 */
	Mine getMineByName(String name);

	/**
	 * Gets a mine by location
	 *
	 * @param loc Location
	 * @return Mine.class
	 */
	Mine getMineAtLocation(Location loc);

	/**
	 * Creates a mine with given mine selection positions and name
	 * @param mineSelection
	 * @param name
	 */
	Mine createMine(MineSelection mineSelection, String name);

	/**
	 * Deletes a mine
	 * @param mine Mine
	 */
	boolean deleteMine(Mine mine);

	/**
	 * Resets a mine
	 * @param mine Mine
	 */
	void resetMine(Mine mine);
}
