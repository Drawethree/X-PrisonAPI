package dev.drawethree.xprison.api.enchants.model;

/**
 * Represents an enchantment that supports a prestige system.
 * <p>
 * When a player reaches the maximum level of a prestigeable enchant and has
 * accumulated enough activations, they can prestige it: the enchant level resets
 * to zero and a permanent multiplier bonus is applied to its reward.
 */
public interface PrestigeableEnchant {

    /**
     * Returns whether the prestige system is enabled for this enchantment.
     *
     * @return {@code true} if prestige is enabled, {@code false} otherwise.
     */
    boolean isPrestigeEnabled();

    /**
     * Returns the maximum prestige tier this enchantment can reach.
     *
     * @return The maximum prestige level.
     */
    int getMaxPrestige();

    /**
     * Returns the reward multiplier added per prestige tier.
     * <p>
     * The total multiplier at prestige {@code p} is {@code 1.0 + p * multiplierPerPrestige}.
     *
     * @return The multiplier increment per prestige tier.
     */
    double getMultiplierPerPrestige();

    /**
     * Returns the number of enchant activations required to prestige from the given tier.
     * <p>
     * {@code currentPrestige} is the tier the player is currently at (before prestiging).
     * Passes {@code currentPrestige} as the {@code prestige} variable in the configured formula.
     *
     * @param currentPrestige The player's current prestige tier.
     * @return The number of activations required to advance to the next prestige.
     */
    long getRequiredActivations(int currentPrestige);
}
