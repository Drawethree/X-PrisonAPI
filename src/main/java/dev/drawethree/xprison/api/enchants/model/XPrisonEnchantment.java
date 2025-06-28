package dev.drawethree.xprison.api.enchants.model;

import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeLevel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

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

    /**
     * Gets the slot number in the enchanting GUI where this enchantment is displayed.
     *
     * @return The GUI slot index.
     */
    int getGuiSlot();

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
     * Gets the required pickaxe level before this enchantment can be applied.
     *
     * @return The required pickaxe level.
     */
    int getRequiredPickaxeLevel();

    /**
     * Reloads this enchantment's configuration or data.
     */
    void reload();
}
