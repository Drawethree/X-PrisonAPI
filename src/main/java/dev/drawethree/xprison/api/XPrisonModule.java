package dev.drawethree.xprison.api;

/**
 * Represents a module within the XPrison plugin ecosystem.
 */
public interface XPrisonModule {

    /**
     * Checks if the module is currently enabled.
     *
     * @return true if the module is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Gets the name of the module.
     *
     * @return the module's name
     */
    String getName();

    /**
     * Checks if history tracking is enabled for this module.
     *
     * @return true if history tracking is enabled, false otherwise
     */
    boolean isHistoryEnabled();
}
