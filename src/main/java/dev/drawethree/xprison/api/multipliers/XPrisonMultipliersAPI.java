package dev.drawethree.xprison.api.multipliers;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.multipliers.model.Multiplier;
import dev.drawethree.xprison.api.multipliers.model.PlayerMultiplier;
import org.bukkit.entity.Player;

/**
 * API interface for managing multipliers related to sell amounts and tokens.
 */
public interface XPrisonMultipliersAPI {

	/**
	 * Gets the  global  multiplier for currency
	 *
	 * @return the global sell {@link Multiplier}
	 */
	Multiplier getGlobalMultiplier(XPrisonCurrency currency);

	/**
	 * Gets the player's sell multiplier.
	 *
	 * @param p the player
	 * @return the {@link PlayerMultiplier} for selling
	 */
	PlayerMultiplier getPlayerMultiplier(Player p, XPrisonCurrency currency);

}
