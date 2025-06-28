package dev.drawethree.xprison.api.multipliers;

import dev.drawethree.xprison.api.multipliers.model.Multiplier;
import dev.drawethree.xprison.api.multipliers.model.MultiplierType;
import dev.drawethree.xprison.api.multipliers.model.PlayerMultiplier;
import org.bukkit.entity.Player;

/**
 * API interface for managing multipliers related to sell amounts and tokens.
 */
public interface XPrisonMultipliersAPI {

	/**
	 * Gets the current global sell multiplier.
	 *
	 * @return the global sell {@link Multiplier}
	 */
	Multiplier getGlobalSellMultiplier();

	/**
	 * Gets the current global token multiplier.
	 *
	 * @return the global token {@link Multiplier}
	 */
	Multiplier getGlobalTokenMultiplier();

	/**
	 * Gets the player's sell multiplier.
	 *
	 * @param p the player
	 * @return the {@link PlayerMultiplier} for selling
	 */
	PlayerMultiplier getSellMultiplier(Player p);

	/**
	 * Gets the player's token multiplier.
	 *
	 * @param p the player
	 * @return the {@link PlayerMultiplier} for tokens
	 */
	PlayerMultiplier getTokenMultiplier(Player p);

	/**
	 * Gets the player's rank multiplier.
	 *
	 * @param p the player
	 * @return the {@link PlayerMultiplier} for the player's rank
	 */
	PlayerMultiplier getRankMultiplier(Player p);

	/**
	 * Gets the overall multiplier for a player based on the specified multiplier type.
	 *
	 * @param p              the player
	 * @param multiplierType the type of multiplier (SELL / TOKENS)
	 * @return the overall multiplier as a double
	 */
	double getPlayerMultiplier(Player p, MultiplierType multiplierType);

	/**
	 * Calculates the total amount to deposit after applying the player's multiplier.
	 *
	 * @param p       the player
	 * @param deposit the original amount to deposit
	 * @param type    the multiplier type (tokens or money)
	 * @return the new amount to deposit after applying multiplier
	 */
	default double getTotalToDeposit(Player p, double deposit, MultiplierType type) {
		return deposit * (1.0 + this.getPlayerMultiplier(p, type));
	}
}
