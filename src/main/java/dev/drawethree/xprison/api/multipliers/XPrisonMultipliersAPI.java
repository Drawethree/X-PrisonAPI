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


	/**
	 * Calculates the total amount to deposit after applying the player's multiplier.
	 *
	 * @param p       the player
	 * @param deposit the original amount to deposit
	 * @param currency    the currency affected
	 * @return the new amount to deposit after applying multiplier
	 */
	default double getTotalToDeposit(Player p, double deposit, XPrisonCurrency currency) {
		PlayerMultiplier multiplier = this.getPlayerMultiplier(p, currency);
		if (multiplier == null) {
			return deposit;
		}
		return deposit * (1.0 + multiplier.getMultiplier());
	}
}
