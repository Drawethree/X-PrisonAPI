package dev.drawethree.xprison.api.enchants.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.List;

/**
 * Implementation of {@link XPrisonEnchantmentGuiProperties} representing
 * the GUI properties for a custom enchantment in the XPrison system.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
class XPrisonEnchantmentGuiPropertiesBase implements XPrisonEnchantmentGuiProperties {

    /**
     * The slot in the GUI where this enchantment's icon will appear.
     */
    private int guiSlot;

    /**
     * The display name of the enchantment in the GUI (may contain color codes).
     */
    private String guiName;

    /**
     * The base64 encoded texture data for custom item icons (nullable).
     */
    private String guiBase64;

    /**
     * The material used as the icon for this enchantment in the GUI.
     */
    private Material guiMaterial;

    /**
     * The description lore lines for this enchantment in the GUI.
     */
    private List<String> guiDescription;

}
