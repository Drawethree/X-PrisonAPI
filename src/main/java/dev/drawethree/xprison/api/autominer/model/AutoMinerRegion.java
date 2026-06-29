package dev.drawethree.xprison.api.autominer.model;

import org.bukkit.World;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.Collection;
import java.util.Map;

public interface AutoMinerRegion {

    /**
     * World where region is based
     * @return {@link World}}
     */
    World getWorld();

    /**
     * Region where autominer is set
     * @return {@link IWrappedRegion}}
     */
    IWrappedRegion getRegion();

    /**
     * The WorldGuard region name (id) this AutoMiner region is bound to. Convenience accessor
     * that does not require the WorldGuard wrapper types on the caller's classpath.
     *
     * @return the region name
     */
    String getRegionName();

    /**
     * Command rewards to execute when player is automining in this region
     * @return list of command rewards
     */
    Collection<String> getCommands();

    /**
     * Period after which player gets rewards and blocks broken
     * @return reward period in seconds
     */
    int getRewardPeriod();

    /**
     * Amount of blocks that will be added to player per reward period
     * @return amount of blocks broken to add
     */
    int getBlocksBroken();

    /**
     * The weighted block pool this region's AutoMiner simulates mining from.
     * Weights are relative (they need not total 100).
     *
     * @return a map of material name to weight; empty if the region has no block pool
     *         (i.e. it runs legacy command rewards instead)
     */
    Map<String, Double> getBlockPoolWeights();

    /**
     * Sets (adds or updates) the relative weight of a block in this region's block pool.
     * The change is applied in memory immediately; persist it by passing this region to
     * {@code XPrisonAutoMinerAPI#saveRegion(AutoMinerRegion)}.
     *
     * @param material the material name (e.g. {@code "DIAMOND_ORE"})
     * @param weight   the relative weight (must be positive)
     */
    void setBlockWeight(String material, double weight);

    /**
     * Removes a block from this region's block pool. The change is applied in memory
     * immediately; persist it by passing this region to
     * {@code XPrisonAutoMinerAPI#saveRegion(AutoMinerRegion)}.
     *
     * @param material the material name to remove
     */
    void removeBlockWeight(String material);

}