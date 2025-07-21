package dev.drawethree.xprison.api.miningstats.model;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Represents a player's active mining session stats.
 */
public interface MiningStats {

    /**
     * Get the player this stats object belongs to.
     *
     * @return Player
     */
    Player getPlayer();

    /**
     * Get total blocks mined during the session.
     *
     * @return number of blocks mined
     */
    int getBlocksMined();

    /**
     * Get the currencies earned in the session.
     *
     * @return Map of currency → amount earned
     */
    Map<XPrisonCurrency, Double> getCurrenciesEarned();

    /**
     * Get how many times each enchantment has procced.
     *
     * @return Map of enchantment → proc count
     */
    Map<XPrisonEnchantment, Integer> getEnchantProcs();
}
