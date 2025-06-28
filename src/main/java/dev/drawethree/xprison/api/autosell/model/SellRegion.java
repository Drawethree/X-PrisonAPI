package dev.drawethree.xprison.api.autosell.model;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

/**
 * Represents a region where auto-selling blocks or items is allowed.
 * Contains methods to manage sell prices and permissions within the region.
 */
public interface SellRegion {

    /**
     * Gets the WorldGuard region this SellRegion represents.
     *
     * @return the wrapped WorldGuard region
     */
    IWrappedRegion getRegion();

    /**
     * Gets the {@link World} where this region is located.
     *
     * @return the world of the region
     */
    World getWorld();

    /**
     * Gets the permission node required for a player to sell items in this region.
     *
     * @return the required permission string
     */
    String getRequiredPermission();

    /**
     * Adds or updates the sell price for a specific material in this region.
     *
     * @param material the material to set the sell price for
     * @param price    the price at which the material will be sold
     */
    void addSellPrice(XMaterial material, double price);

    /**
     * Removes a material from being sellable in this region.
     *
     * @param material the material to remove from the sell price list
     */
    void removeSellPrice(XMaterial material);

    /**
     * Gets the sell price for a specific material in this region.
     *
     * @param material the material to get the price for
     * @return the sell price of the material, or 0 if not sellable
     */
    double getSellPriceForMaterial(XMaterial material);

    /**
     * Checks whether a given {@link Location} is inside this sell region.
     *
     * @param location the location to check
     * @return true if the location is inside the region, false otherwise
     */
    boolean contains(Location location);

    /**
     * Checks if the given player has permission to sell in this region.
     * This is based on {@link SellRegion#getRequiredPermission()}.
     *
     * @param player the player to check
     * @return true if the player can sell in the region, false otherwise
     */
    boolean canPlayerSellInRegion(Player player);
}
