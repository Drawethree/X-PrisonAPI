package dev.drawethree.xprison.api.autominer;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    /**
     * Finds an AutoMiner region by its WorldGuard region name.
     *
     * @param name the region name (case-insensitive)
     * @return an Optional containing the matching region, or empty if not found
     */
    Optional<AutoMinerRegion> getAutoMinerRegionByName(String name);

    /**
     * Adds time to the AutoMiner for the given online player.
     *
     * @param player  the online player
     * @param seconds the number of seconds to add (must be positive)
     */
    void addAutoMinerTime(Player player, int seconds);

    /**
     * Sets the AutoMiner time for the given online player, replacing any existing value.
     *
     * @param player  the online player
     * @param seconds the new time in seconds (clamped to 0 if negative)
     */
    void setAutoMinerTime(Player player, int seconds);

    /**
     * Removes time from the AutoMiner for the given online player.
     * The result is clamped at 0; it will never go negative.
     *
     * @param player  the online player
     * @param seconds the number of seconds to remove (must be positive)
     */
    void removeAutoMinerTime(Player player, int seconds);

    /**
     * Returns the top N players by remaining AutoMiner time, ordered descending.
     *
     * @param limit maximum number of entries to return
     * @return ordered map of UUID → remaining seconds
     */
    Map<UUID, Integer> getTopByAutoMinerTime(int limit);
}
