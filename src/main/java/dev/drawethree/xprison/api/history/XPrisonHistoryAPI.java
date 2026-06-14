package dev.drawethree.xprison.api.history;

import dev.drawethree.xprison.api.XPrisonModule;
import dev.drawethree.xprison.api.history.model.HistoryLine;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

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

	/**
	 * Returns a paginated slice of the player's full history, sorted by date descending.
	 *
	 * @param player   the player whose history is requested
	 * @param page     1-based page number
	 * @param pageSize number of entries per page
	 * @return the requested page of history lines
	 */
	Collection<HistoryLine> getPlayerHistory(OfflinePlayer player, int page, int pageSize);

	/**
	 * Returns the total number of history entries for the given player.
	 *
	 * @param player the player to count history lines for
	 * @return total history entry count
	 */
	int getPlayerHistoryCount(OfflinePlayer player);

	/**
	 * Asynchronous variant of {@link #getPlayerHistory(OfflinePlayer)} that runs the query off the server thread.
	 *
	 * @param player the player whose history is requested
	 * @return a future completing with the player's full history
	 */
	@NotNull
	default CompletableFuture<Collection<HistoryLine>> getPlayerHistoryAsync(OfflinePlayer player) {
		return CompletableFuture.supplyAsync(() -> getPlayerHistory(player));
	}

	/**
	 * Asynchronous variant of {@link #getPlayerHistory(OfflinePlayer, int, int)} that runs the query off the server thread.
	 *
	 * @param player   the player whose history is requested
	 * @param page     1-based page number
	 * @param pageSize number of entries per page
	 * @return a future completing with the requested page of history lines
	 */
	@NotNull
	default CompletableFuture<Collection<HistoryLine>> getPlayerHistoryAsync(OfflinePlayer player, int page, int pageSize) {
		return CompletableFuture.supplyAsync(() -> getPlayerHistory(player, page, pageSize));
	}
}
