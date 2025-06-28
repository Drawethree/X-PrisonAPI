package dev.drawethree.xprison.api.tokens;

import dev.drawethree.xprison.api.shared.currency.enums.XPrisonCurrency;
import org.bukkit.OfflinePlayer;

/**
 * API for managing player tokens.
 */
public interface XPrisonTokensAPI extends XPrisonCurrency {

	/**
	 * Checks if a player has at least a specified amount of tokens.
	 *
	 * @param p      the player
	 * @param amount the amount to check against
	 * @return true if the player has at least the specified amount, false otherwise
	 */
	boolean hasEnough(OfflinePlayer p, long amount);

}
