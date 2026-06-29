package dev.drawethree.xprison.api.autominer.model;

/**
 * An AutoMiner upgrade tier as defined in {@code autominer.yml}.
 * <p>
 * A tier describes the <i>quality</i> of a player's AutoMiner: how many blocks are simulated
 * per reward cycle, how often a cycle fires, the flat multiplier applied to the simulated
 * income, and the cost (and currency) to upgrade into this tier.
 */
public interface AutoMinerTier {

    /**
     * @return the 1-based tier number
     */
    int getTier();

    /**
     * @return the display name shown in the AutoMiner GUI (may contain colour codes)
     */
    String getDisplayName();

    /**
     * Sets the display name shown in the AutoMiner GUI (may contain colour codes).
     *
     * @param displayName the new display name
     */
    void setDisplayName(String displayName);

    /**
     * @return the icon material name (e.g. {@code "DIAMOND_PICKAXE"})
     */
    String getIcon();

    /**
     * Sets the icon material name (e.g. {@code "DIAMOND_PICKAXE"}).
     *
     * @param icon the new icon material name
     */
    void setIcon(String icon);

    /**
     * @return how many blocks are simulated per reward cycle
     */
    int getBlocksPerCycle();

    /**
     * Sets how many blocks are simulated per reward cycle.
     *
     * @param blocksPerCycle the new blocks-per-cycle value (must be positive)
     */
    void setBlocksPerCycle(int blocksPerCycle);

    /**
     * @return the flat multiplier applied to this tier's simulated mining income
     */
    double getRewardMultiplier();

    /**
     * Sets the flat multiplier applied to this tier's simulated mining income.
     *
     * @param rewardMultiplier the new reward multiplier
     */
    void setRewardMultiplier(double rewardMultiplier);

    /**
     * @return the number of seconds between reward cycles for a player on this tier
     */
    int getRewardPeriod();

    /**
     * Sets the number of seconds between reward cycles for a player on this tier.
     *
     * @param rewardPeriod the new reward period in seconds (must be positive)
     */
    void setRewardPeriod(int rewardPeriod);

    /**
     * @return the cost to upgrade into this tier (0 for the starting tier)
     */
    double getUpgradeCost();

    /**
     * Sets the cost to upgrade into this tier.
     *
     * @param upgradeCost the new upgrade cost
     */
    void setUpgradeCost(double upgradeCost);

    /**
     * @return the name of the currency used to pay {@link #getUpgradeCost()}
     */
    String getUpgradeCurrency();

    /**
     * Sets the name of the currency used to pay {@link #getUpgradeCost()}.
     *
     * @param upgradeCurrency the new upgrade currency name
     */
    void setUpgradeCurrency(String upgradeCurrency);
}
