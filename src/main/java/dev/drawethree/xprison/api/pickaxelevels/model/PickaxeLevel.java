package dev.drawethree.xprison.api.pickaxelevels.model;

/**
 * Represents a pickaxe level in the XPrison system.
 */
public interface PickaxeLevel {

    /**
     * Gets the numeric level of the pickaxe.
     *
     * @return the level number
     */
    int getLevel();

    /**
     * Gets the number of blocks required to reach this pickaxe level.
     *
     * @return the amount of blocks required
     */
    long getBlocksRequired();

    /**
     * Gets the display name of the pickaxe level.
     *
     * @return the display name
     */
    String getDisplayName();

}
