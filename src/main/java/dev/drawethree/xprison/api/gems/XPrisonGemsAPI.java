package dev.drawethree.xprison.api.gems;

import dev.drawethree.xprison.api.shared.currency.enums.XPrisonCurrency;
import org.bukkit.OfflinePlayer;

/**
 * API interface for interacting with the Gems system in the XPrison plugin.
 * Provides methods to get, add, remove, and set gems for players, as well as to check gem balances.
 */
public interface XPrisonGemsAPI extends XPrisonCurrency {

	/**
	 * Checks if the specified player has at least the given amount of gems.
	 *
	 * @param player the {@link OfflinePlayer} to check
	 * @param amount the minimum amount of gems required
	 * @return {@code true} if the player has at least the specified amount of gems, {@code false} otherwise
	 */
	boolean hasEnough(OfflinePlayer player, long amount);

}
