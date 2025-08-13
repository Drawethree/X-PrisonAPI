package dev.drawethree.xprison.api.enchants.model;

import dev.drawethree.xprison.api.XPrisonAPI;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;

/**
 * Represents a custom enchantment in the XPrison system.
 * Each enchantment has a unique ID, name, configuration, and purchasing behavior.
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

    /**
     * Gets the GUI properties of this enchantment, such as material, slot, and description.
     *
     * @return The GUI properties of the enchantment.
     */
    XPrisonEnchantmentGuiProperties getGuiProperties();

    /**
     * Gets the name of the developer or plugin author who created this enchantment.
     *
     * @return The author name.
     */
    String getAuthor();

    /**
     * Checks whether this enchantment is currently enabled in the system.
     *
     * @return True if the enchantment is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Gets the maximum level this enchantment can be upgraded to.
     *
     * @return The maximum allowed enchantment level.
     */
    int getMaxLevel();

    /**
     * Gets the base cost of the enchantment, used as the starting cost for level 1.
     *
     * @return The base cost of applying the enchantment.
     */
    long getBaseCost();

    /**
     * Gets the cost of this enchantment at the specified level.
     *
     * @param level The enchantment level.
     * @return The cost at that level.
     */
    long getCostAtLevel(int level);

    /**
     * Initializes or loads this enchantment. Called during plugin load or reload.
     */
    void load();

    /**
     * Cleans up or unloads this enchantment. Called during plugin shutdown or reload.
     */
    void unload();

    /**
     * Gets the name of the currency used to purchase or upgrade this enchantment.
     * <p>
     * This defines which type of currency (e.g., "Tokens", "Gems", "Vault") will be consumed
     * when a player interacts with this enchant. The name should match a registered currency
     * in the XPrison currency system.
     *
     * @return The name of the currency used for this enchantment.
     */
    String getCurrencyName();

    /**
     * Retrieves the {@link XPrisonCurrency} instance associated with the currency name returned by {@link #getCurrencyName()}.
     * <p>
     * This is a convenience method to directly access the currency object via the XPrison API,
     * typically used to format amounts, apply caps, or access metadata like display name or symbol.
     *
     * @return The {@link XPrisonCurrency} instance, or {@code null} if the currency is not registered.
     */
    default XPrisonCurrency getCurrency() {
        return XPrisonAPI.getInstance().getCurrencyApi().getCurrency(getCurrencyName());
    }

}
