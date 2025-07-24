package dev.drawethree.xprison.api.currency.model;

import org.bukkit.inventory.ItemStack;

public interface PhysicalItemCurrency {

    /**
     * Gets the configured physical item for this currency, if any.
     * This can be used in GUIs or displays to represent the currency visually.
     *
     * @return The {@link ItemStack} used as the item, or null if not set.
     */
    ItemStack getPhysicalItem();
}