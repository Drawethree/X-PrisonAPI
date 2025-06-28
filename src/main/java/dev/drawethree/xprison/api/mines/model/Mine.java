package dev.drawethree.xprison.api.mines.model;

import me.lucko.helper.serialize.Point;
import me.lucko.helper.serialize.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Date;

/**
 * Represents a mine area with associated properties and behaviors.
 */
public interface Mine {

    /**
     * Gets the name of the mine.
     *
     * @return the name of the mine
     */
    String getName();

    /**
     * Gets the region that defines the boundaries of the mine.
     *
     * @return the {@link Region} of the mine
     */
    Region getRegion();

    /**
     * Gets the teleport location inside the mine.
     *
     * @return the {@link Point} where players are teleported within the mine
     */
    Point getTeleportLocation();

    /**
     * Gets the block palette associated with the mine.
     *
     * @return the {@link BlockPalette} used for this mine
     */
    BlockPalette getBlockPalette();

    /**
     * Gets the percentage of blocks that trigger a reset of the mine.
     *
     * @return the reset percentage as a double (e.g., 0.2 for 20%)
     */
    double getResetPercentage();

    /**
     * Gets the total number of blocks in the mine.
     *
     * @return total block count
     */
    int getTotalBlocks();

    /**
     * Gets the current number of blocks remaining in the mine.
     *
     * @return current block count
     */
    int getCurrentBlocks();

    /**
     * Returns whether the mine is currently resetting.
     *
     * @return true if the mine is resetting, false otherwise
     */
    boolean isResetting();

    /**
     * Gets the next scheduled reset date/time of the mine.
     *
     * @return the {@link Date} when the mine will next reset
     */
    Date getNextResetDate();

    /**
     * Checks if a given location is inside the mine.
     *
     * @param location the {@link Location} to check
     * @return true if the location is inside the mine, false otherwise
     */
    boolean isInMine(Location location);

    /**
     * Gets all players currently inside the mine.
     *
     * @return a collection of players inside the mine
     */
    Collection<Player> getPlayersInMine();

    /**
     * Adds a potion effect to all players in the mine.
     *
     * @param potionEffectType the type of potion effect to add
     * @param level            the level (amplifier) of the effect
     */
    void addEffect(PotionEffectType potionEffectType, int level);

    /**
     * Removes a potion effect from all players in the mine.
     *
     * @param potionEffectType the type of potion effect to remove
     */
    void removeEffect(PotionEffectType potionEffectType);
}
