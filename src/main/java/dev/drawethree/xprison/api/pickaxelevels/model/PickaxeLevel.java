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
     * Gets the amount of pickaxe experience required to reach this pickaxe level.
     * <p>
     * By default every broken block grants 1 experience, so for servers without custom
     * per-block values or source multipliers this equals the historical blocks-required
     * threshold.
     *
     * @return the amount of experience required
     */
    long getExpRequired();

    /**
     * Gets the number of blocks required to reach this pickaxe level.
     *
     * @return the amount of blocks required
     * @deprecated pickaxe levels are driven by experience since 1.9; this is an alias of
     * {@link #getExpRequired()} kept for backwards compatibility.
     */
    @Deprecated
    default long getBlocksRequired() {
        return getExpRequired();
    }

    /**
     * Gets the display name of the pickaxe level.
     *
     * @return the display name
     */
    String getDisplayName();

}
