package dev.drawethree.xprison.api.autominer;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * API interface for AutoMiner feature.
 */
public interface XPrisonAutoMinerAPI {

    /**
     * Checks if a player is currently inside an AutoMiner region.
     *
     * @param player The player to check
     * @return true if the player is in an AutoMiner region, false otherwise
     */
    boolean isInAutoMinerRegion(Player player);

    /**
     * Gets the remaining AutoMiner time for the specified player.
     *
     * @param player The player whose AutoMiner time to get
     * @return Remaining AutoMiner time in seconds
     */
    int getAutoMinerTime(Player player);

    /**
     * Retrieves a collection of all loaded AutoMiner regions.
     *
     * @return A collection containing all AutoMiner regions
     */
    Collection<AutoMinerRegion> getAutoMinerRegions();
}
