package dev.drawethree.xprison.api.history;

import dev.drawethree.xprison.api.XPrisonModule;
import dev.drawethree.xprison.api.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

/**
 * API for interacting with player history within the XPrison plugin.
 * Provides methods for retrieving and creating {@link HistoryLine} entries
 * related to specific {@link XPrisonModule}s and players.
 */
public interface XPrisonHistoryAPI {

	/**
	 * Retrieves all history lines associated with the given player across all modules.
	 *
	 * @param player the {@link OfflinePlayer} whose history is being requested
	 * @return a {@link Collection} of {@link HistoryLine} representing the player's full history
	 */
	Collection<HistoryLine> getPlayerHistory(OfflinePlayer player);

	/**
	 * Retrieves the history lines associated with the given player and a specific module.
	 *
	 * @param player the {@link OfflinePlayer} whose history is being requested
	 * @param module the {@link XPrisonModule} to filter history lines by
	 * @return a {@link Collection} of {@link HistoryLine} filtered by the specified module
	 */
	Collection<HistoryLine> getPlayerHistory(OfflinePlayer player, XPrisonModule module);

	/**
	 * Creates a new history line entry for the specified player, associated with the provided module and context.
	 *
	 * @param player  the {@link OfflinePlayer} for whom the history line is created
	 * @param module  the {@link XPrisonModule} that the history line is associated with
	 * @param context a {@link String} describing the context or reason for the history entry
	 * @return the created {@link HistoryLine}
	 */
	HistoryLine createHistoryLine(OfflinePlayer player, XPrisonModule module, String context);
}
