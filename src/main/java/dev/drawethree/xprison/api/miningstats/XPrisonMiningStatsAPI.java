package dev.drawethree.xprison.api.miningstats;

import dev.drawethree.xprison.api.miningstats.model.MiningStats;
import org.bukkit.entity.Player;


/**
 * API for managing online mining stats  in XPrison.
 */
public interface XPrisonMiningStatsAPI {

    /**
     * Gets the mining stats for the given player if available.
     *
     * @param player Player to get stats for
     * @return mining stats
     */
    MiningStats getStats(Player player);

}
