package dev.drawethree.xprison.api.enchants.model;

import org.bukkit.Material;

import java.util.Collection;

public interface XPrisonEnchantmentGuiProperties {

    /**
     * Gets the slot number in the enchanting GUI where this enchantment is displayed.
     *
     * @return The GUI slot index.
     */
    int getGuiSlot();

    /**
     * Gets the page of the enchanting GUI this enchantment is displayed on.
     * <p>
     * Pages are 1-based, so {@code 1} is the first page. Enchantments that do not fit the
     * page they ask for - because the slot is already taken, or falls outside the menu's
     * configured content region - are moved to the next free slot automatically, spilling
     * onto additional pages as needed.
     *
     * @return the 1-based GUI page; defaults to {@code 1}.
     * @since 1.9
     */
    default int getGuiPage() {
        return 1;
    }

    /**
     * Gets the material type of the GUI item representing this enchantment.
     *
     * @return The material used in the GUI.
     */
    Material getGuiMaterial();

    /**
     * Gets the display name of the GUI item representing this enchantment.
     * May contain color codes.
     *
     * @return The GUI item name.
     */
    String getGuiName();

    /**
     * Gets the lore (description) of the GUI item for this enchantment.
     * May contain color codes.
     *
     * @return A collection of lore strings.
     */
    Collection<String> getGuiDescription();

    /**
     * Gets the Base64 texture data for the GUI item, useful if using a custom player head.
     *
     * @return The Base64 string for the GUI item texture, or null/empty if not applicable.
     */
    String getGuiBase64();

    /**
     * Gets the custom model data for the GUI item
     *
     * @return Custom Model Data of GUI item
     */
    int getCustomModelData();
}