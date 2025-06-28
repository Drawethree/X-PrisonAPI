package dev.drawethree.xprison.api.history;

import dev.drawethree.xprison.api.XPrisonModule;
import dev.drawethree.xprison.api.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.List;

public interface XPrisonHistoryAPI {

	/**
	 * Gets players history
	 *
	 * @param player Player
	 * @return List containing all HistoryLine.class of Player
	 */
	Collection<HistoryLine> getPlayerHistory(OfflinePlayer player);

	/**
	 * Gets players history for respective module
	 *
	 * @param player Player
	 * @return List containing all HistoryLine.class of Player
	 */
	Collection<HistoryLine> getPlayerHistory(OfflinePlayer player, XPrisonModule module);

	/**
	 * Creates a new history line for player
	 *
	 * @param player  Player
	 * @param context Context of the history
	 * @param module  XPrisonModule associated with the history
	 */
	HistoryLine createHistoryLine(OfflinePlayer player, XPrisonModule module, String context);
}
