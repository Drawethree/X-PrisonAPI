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
	 * Gets the global multiplier for a specific currency.
	 *
	 * @param currency the {@link XPrisonCurrency} to get the multiplier for
	 * @return the global {@link Multiplier} for the given currency
	 */
	Multiplier getGlobalMultiplier(XPrisonCurrency currency);

	/**
	 * Gets the player's multiplier for a specific currency.
	 *
	 * @param player   the {@link Player} whose multiplier is being retrieved
	 * @param currency the {@link XPrisonCurrency} to get the multiplier for
	 * @return the {@link PlayerMultiplier} for the given player and currency
	 */
	PlayerMultiplier getPlayerMultiplier(Player player, XPrisonCurrency currency);

	/**
	 * Gets the player's rank-based multiplier for a specific currency.
	 *
	 * @param player   the {@link Player} whose rank multiplier is being retrieved
	 * @param currency the {@link XPrisonCurrency} to get the rank multiplier for
	 * @return the {@link PlayerMultiplier} representing the player's rank multiplier
	 */
	PlayerMultiplier getRankMultiplier(Player player, XPrisonCurrency currency);

}
