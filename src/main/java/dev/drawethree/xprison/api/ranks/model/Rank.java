package dev.drawethree.xprison.api.ranks.model;

/**
 * Represents a rank in the XPrison system.
 */
public interface Rank {

    /**
     * Gets the unique identifier of this rank.
     *
     * @return the rank ID
     */
    int getId();

    /**
     * Gets the cost required to obtain or rank up to this rank.
     *
     * @return the cost of the rank
     */
    double getCost();

    /**
     * Gets the prefix string associated with this rank,
     * typically used for display in chat or player lists.
     *
     * @return the prefix of the rank
     */
    String getPrefix();
}
