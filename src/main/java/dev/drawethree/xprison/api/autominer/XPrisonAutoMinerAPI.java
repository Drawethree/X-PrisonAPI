package dev.drawethree.xprison.api.autominer;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import dev.drawethree.xprison.api.shared.Pagination;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
     * Gets the player's current AutoMiner tier (1-based).
     *
     * @param player the online player
     * @return the current tier number
     */
    int getTier(Player player);

    /**
     * Sets the player's AutoMiner tier (clamped to a minimum of 1).
     *
     * @param player the online player
     * @param tier   the new tier number
     */
    void setTier(Player player, int tier);

    /**
     * Gets the lifetime amount of blocks this player's AutoMiner has mined.
     *
     * @param player the online player
     * @return total blocks mined
     */
    long getBlocksMined(Player player);

    /**
     * Gets the lifetime amount of currency this player's AutoMiner has earned.
     *
     * @param player the online player
     * @return total currency earned
     */
    double getMoneyEarned(Player player);

    /**
     * Estimates the average income a single reward cycle would produce for the
     * player at their current tier inside the region they are standing in.
     *
     * @param player the online player
     * @return estimated income per cycle, or 0 if not currently computable
     */
    double getEstimatedIncomePerCycle(Player player);

    /**
     * Returns the top N players by remaining AutoMiner time, ordered descending.
     *
     * @param limit maximum number of entries to return
     * @return ordered map of UUID → remaining seconds
     */
    Map<UUID, Integer> getTopByAutoMinerTime(int limit);

    /**
     * Paginated variant of {@link #getTopByAutoMinerTime(int)}.
     * <p>
     * The default implementation over-fetches and slices the ordered result client-side.
     *
     * @param limit  maximum number of entries to return
     * @param offset number of leading entries to skip (0 = start from top)
     * @return ordered map of UUID → remaining seconds
     */
    @NotNull
    default Map<UUID, Integer> getTopByAutoMinerTime(int limit, int offset) {
        return Pagination.slice(getTopByAutoMinerTime(limit + Math.max(offset, 0)), limit, offset);
    }
}
