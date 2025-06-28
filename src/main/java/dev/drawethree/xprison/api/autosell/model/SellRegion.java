package dev.drawethree.xprison.api.autosell.model;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public interface SellRegion {

    IWrappedRegion getRegion();

    World getWorld();

    String getRequiredPermission();

    void addSellPrice(XMaterial material, double price);

    void removeSellPrice(XMaterial material);

    double getSellPriceForMaterial(XMaterial material);

    boolean contains(Location location);

    boolean canPlayerSellInRegion(Player player);

}