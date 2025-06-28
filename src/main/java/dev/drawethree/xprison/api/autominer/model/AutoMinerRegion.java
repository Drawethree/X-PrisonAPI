package dev.drawethree.xprison.api.autominer.model;

import org.bukkit.World;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.Collection;

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


}