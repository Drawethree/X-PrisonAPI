package dev.drawethree.xprison.api.enchants.model;


/**
 * Represents a custom enchantment in the XPrison system.
 */
public interface XPrisonEnchantment {

    /**
     * Gets the unique ID of this enchantment.
     *
     * @return The unique enchantment ID.
     */
    int getId();

    /**
     * Gets the raw name of the enchantment (without color codes).
     *
     * @return The raw enchantment name.
     */
    String getRawName();

    /**
     * Gets the display name of the enchantment, including color codes.
     *
     * @return The colored enchantment name.
     */
    String getName();

    /**
     * Gets the display name of the enchantment without color codes.
     *
     * @return The enchantment name without color codes.
     */
    String getNameWithoutColor();

    XPrisonEnchantmentGuiProperties getGuiProperties();

    /**
     * Gets the author of this enchantment.
     *
     * @return The author name.
     */
    String getAuthor();

    /**
     * Checks whether this enchantment is currently enabled.
     *
     * @return True if the enchantment is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Gets the maximum enchantment level that can be applied.
     *
     * @return The maximum allowed level.
     */
    int getMaxLevel();

    /**
     * Gets the base cost of applying this enchantment.
     *
     * @return The base cost.
     */
    long getBaseCost();

    /**
     * Gets the incremental cost added per level of this enchantment.
     *
     * @return The increase in cost per level.
     */
    long getIncreaseCost();

    /**
     * Loads or initializes the enchantment from its configuration or internal state.
     * Called when the enchantment is first created or reloaded.
     */
    void load();

    /**
     * Unloads or disables the enchantment, freeing resources or unregistering listeners.
     */
    void unload();
}
