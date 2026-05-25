package dev.drawethree.xprison.api.multipliers;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.multipliers.model.Multiplier;
import dev.drawethree.xprison.api.multipliers.model.PlayerMultiplier;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

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

	/**
	 * Returns the combined effective multiplier for a player (global × rank × personal).
	 * Pass 1.0 as a base amount and this returns the full modifier factor.
	 *
	 * @param player   the online player
	 * @param currency the currency to calculate the multiplier for
	 * @return combined multiplier value
	 */
	double getEffectiveMultiplier(Player player, XPrisonCurrency currency);

	/**
	 * Sets the global multiplier for a currency for the given duration.
	 *
	 * @param currency the currency to apply the multiplier to
	 * @param value    the multiplier value (e.g. 2.0 for double)
	 * @param unit     the time unit of the duration
	 * @param duration how long the multiplier lasts
	 */
	void setGlobalMultiplier(XPrisonCurrency currency, double value, TimeUnit unit, int duration);

	/**
	 * Gives a personal multiplier to an online player for the given duration.
	 *
	 * @param player   the player to receive the multiplier
	 * @param currency the currency to apply the multiplier to
	 * @param value    the multiplier value
	 * @param unit     the time unit of the duration
	 * @param duration how long the multiplier lasts
	 */
	void givePlayerMultiplier(Player player, XPrisonCurrency currency, double value, TimeUnit unit, int duration);

	/**
	 * Returns all currently active personal multipliers for the given currency across all online players.
	 *
	 * @param currency the currency to filter by
	 * @return collection of active player multipliers
	 */
	Collection<PlayerMultiplier> getActivePlayerMultipliers(XPrisonCurrency currency);

}
