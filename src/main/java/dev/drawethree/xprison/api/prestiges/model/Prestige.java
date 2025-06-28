package dev.drawethree.xprison.api.prestiges.model;

/**
 * Represents a Prestige level in the XPrison plugin.
 */
public interface Prestige {

    /**
     * Gets the unique identifier of this Prestige.
     *
     * @return the prestige ID
     */
    long getId();

    /**
     * Gets the cost required to reach this Prestige.
     *
     * @return the cost as a double
     */
    double getCost();

    /**
     * Gets the prefix associated with this Prestige (usually for display).
     *
     * @return the prefix string
     */
    String getPrefix();
}
