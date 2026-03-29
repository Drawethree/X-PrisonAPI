package dev.drawethree.xprison.api.autosell.model;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an ItemStack that is intended to be sold within a SellRegion.
 */
public interface AutoSellItemStack {

    /**
     * Gets the {@link ItemStack} representing the item type being sold.
     * The amount of this ItemStack should not be relied on.
     *
     * @return the item stack type
     */
    ItemStack getItemStack();

    /**
     * Gets the amount of items being sold.
     *
     * @return amount of items
     */
    int getAmount();
}