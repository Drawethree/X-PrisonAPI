package dev.drawethree.xprison.api.autosell.model;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an ItemStack that is intended to be sold within a SellRegion.
 */
public interface AutoSellItemStack {

    /**
     * Gets the {@link ItemStack} that will be sold in the sell region.
     *
     * @return the ItemStack to be sold
     */
    ItemStack getItemStack();
}
