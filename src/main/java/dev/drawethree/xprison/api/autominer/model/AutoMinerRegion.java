package dev.drawethree.xprison.api.autominer.model;

import org.bukkit.World;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.Collection;

public interface AutoMinerRegion {

    World getWorld();

    IWrappedRegion getRegion();

    Collection<String> getCommands();

    int getRewardPeriod();

    int getBlocksBroken();


}