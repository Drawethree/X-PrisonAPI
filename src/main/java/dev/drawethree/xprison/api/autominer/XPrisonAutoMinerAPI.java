package dev.drawethree.xprison.api.autominer;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import dev.drawethree.xprison.api.autominer.model.AutoMinerSettings;
import dev.drawethree.xprison.api.autominer.model.AutoMinerTier;
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
     * Returns all configured AutoMiner upgrade tiers, ordered by tier number.
     *
     * @return the tier ladder
     */
    Collection<AutoMinerTier> getTiers();

    // ─── Config editing (dashboard) ──────────────────────────────────────────
    // These mutate autominer.yml and live-reload the affected runtime. Edits made
    // here behave exactly as if the operator had changed the file and run /reload.

    /**
     * Creates a fresh, editable tier for the given tier number, pre-filled with sensible
     * defaults. The tier is <b>not</b> persisted until it is passed to {@link #saveTier(AutoMinerTier)}.
     * To edit an existing tier instead, mutate one obtained from {@link #getTiers()} and save it.
     *
     * @param tier the 1-based tier number this tier will occupy
     * @return a new editable, unsaved tier
     */
    AutoMinerTier createTier(int tier);

    /**
     * Persists a tier (created via {@link #createTier(int)} or obtained from {@link #getTiers()})
     * to {@code autominer.yml}, inserting it if new or overwriting the existing entry with the same
     * tier number. The tier ladder is reloaded so the change takes effect live.
     *
     * @param tier the tier to save
     */
    void saveTier(AutoMinerTier tier);

    /**
     * Removes the tier with the given number from {@code autominer.yml} and reloads the tier ladder.
     *
     * @param tier the 1-based tier number to remove
     */
    void removeTier(int tier);

    /**
     * Persists the current block pool of the given region (obtained from
     * {@link #getAutoMinerRegions()} / {@link #getAutoMinerRegionByName(String)} and mutated via
     * {@link AutoMinerRegion#setBlockWeight(String, double)} / {@link AutoMinerRegion#removeBlockWeight(String)})
     * to {@code autominer.yml}, then restarts the region's AutoMiner task so the change takes effect live.
     *
     * @param region the region to save
     */
    void saveRegion(AutoMinerRegion region);

    /**
     * Returns the current general AutoMiner settings as an editable value object. Mutate it and pass
     * it to {@link #saveSettings(AutoMinerSettings)} to persist.
     *
     * @return the current settings
     */
    AutoMinerSettings getSettings();

    /**
     * Persists the given settings to the {@code settings:} section of {@code autominer.yml} and reloads them.
     *
     * @param settings the settings to save
     */
    void saveSettings(AutoMinerSettings settings);

    // ─── Offline / UUID variants ─────────────────────────────────────────────
    // These work for any player (online or offline), reading from / writing to the
    // database when the player is not online. Use the Player-based methods above when
    // you already have an online player for the cheapest path.

    /**
     * Gets the remaining AutoMiner time for any player by UUID (online or offline).
     *
     * @param uuid the player UUID
     * @return remaining AutoMiner time in seconds, or 0 if unknown
     */
    int getAutoMinerTimeOffline(UUID uuid);

    /**
     * Gets the AutoMiner tier for any player by UUID (online or offline).
     *
     * @param uuid the player UUID
     * @return the current tier number (defaults to 1 if unknown)
     */
    int getTierOffline(UUID uuid);

    /**
     * Gets the lifetime blocks mined by any player's AutoMiner by UUID (online or offline).
     *
     * @param uuid the player UUID
     * @return total blocks mined
     */
    long getBlocksMinedOffline(UUID uuid);

    /**
     * Gets the lifetime currency earned by any player's AutoMiner by UUID (online or offline).
     *
     * @param uuid the player UUID
     * @return total currency earned
     */
    double getMoneyEarnedOffline(UUID uuid);

    /**
     * Sets the AutoMiner time for any player by UUID (online or offline), replacing any
     * existing value. Clamped to 0 if negative.
     *
     * @param uuid    the player UUID
     * @param seconds the new time in seconds
     */
    void setAutoMinerTimeOffline(UUID uuid, int seconds);

    /**
     * Adds AutoMiner time to any player by UUID (online or offline). Pass a negative value
     * to remove time; the result is clamped at 0.
     *
     * @param uuid    the player UUID
     * @param seconds the number of seconds to add (may be negative)
     */
    void addAutoMinerTimeOffline(UUID uuid, int seconds);

    /**
     * Sets the AutoMiner tier for any player by UUID (online or offline). Clamped to a
     * minimum of 1.
     *
     * @param uuid the player UUID
     * @param tier the new tier number
     */
    void setTierOffline(UUID uuid, int tier);

    /**
     * Resets the lifetime AutoMiner stats (blocks mined &amp; money earned) for any player by
     * UUID (online or offline).
     *
     * @param uuid the player UUID
     */
    void resetStatsOffline(UUID uuid);

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
