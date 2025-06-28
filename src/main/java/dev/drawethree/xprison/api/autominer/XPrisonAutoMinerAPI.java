package dev.drawethree.xprison.api.autominer;

import dev.drawethree.xprison.api.autominer.model.AutoMinerRegion;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface XPrisonAutoMinerAPI {

    /**
     * Returns true if player is in autominer region, otherwise return false
     *
     * @param player Player
     * @return returns true if player is in autominer region, otherwise return false
     */
    boolean isInAutoMinerRegion(Player player);

    /**
     * Returns remaining autominer time in seconds for specific player
     *
     * @param player Player
     * @return time in seconds left for specific player autominer
     */
    int getAutoMinerTime(Player player);

    /**
     * Returns Collection of all autominer regions
     *
     * @return Collection of all autominer regions
     */
    Collection<AutoMinerRegion> getAutoMinerRegions();
}
