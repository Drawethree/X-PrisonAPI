package dev.drawethree.xprison.api.rebirth.model;

import org.bukkit.entity.Player;

public interface RebirthRequirement {

    /**
     * @return a short type identifier, e.g. "rank", "prestige", "currency"
     */
    String getType();

    /**
     * @return Whether this requirement is enabled.
     */
    boolean isEnabled();

    /**
     * Checks if a player meets this requirement.
     *
     * @param player the player to check
     * @return true if met
     */
    boolean isMet(Player player);

    /**
     * Applies the "payment" or consequence for this requirement after a rebirth happens.
     * For example:
     * - RANK → reset player rank to default
     * - PRESTIGE → reset player prestige to default
     * - CURRENCY → subtract balance
     *
     * @param player the player to apply the action to
     */
    void apply(Player player);

    /**
     * Returns the failure message to show the player when this requirement is not met.
     */
    String getFailMessage();
}
