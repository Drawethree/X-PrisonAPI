package dev.drawethree.xprison.api.currency.model;

import java.util.List;

/**
 * Represents the serializable configuration of a currency's physical withdraw item.
 * <p>
 * This is distinct from {@link PhysicalItemCurrency}, which produces live {@code ItemStack}
 * objects. This interface exposes the raw configuration values so they can be persisted,
 * transferred, or displayed (e.g., in a web dashboard) without a running Bukkit context.
 */
public interface XPrisonCurrencyItemConfig {

    /**
     * Returns the XMaterial name of this item (e.g., {@code "PAPER"}, {@code "EMERALD"}).
     */
    String getMaterialName();

    /**
     * Returns the custom model data value used for resource-pack overrides. 0 means none.
     */
    int getCustomModelData();

    /**
     * Gets the item model applied to the physical item, as a namespaced key such as {@code nexo:ruby_pickaxe}.
     * <p>
     * Only applied on servers that support the {@code item_model} item component (1.21.3+).
     *
     * @return the item model key, or {@code null} if none is configured
     * @since 1.10
     */
    default String getItemModel() {
        return null;
    }

    /**
     * Gets the tooltip style applied to the physical item, as a namespaced key such as {@code nexo:legendary}.
     * <p>
     * Only applied on servers that support the {@code tooltip_style} item component (1.21.3+).
     *
     * @return the tooltip style key, or {@code null} if none is configured
     * @since 1.10
     */
    default String getTooltipStyle() {
        return null;
    }

    /**
     * Returns the item display-name template. May contain {@code %amount%} as a placeholder.
     */
    String getItemName();

    /**
     * Returns the lore lines for this item.
     */
    List<String> getItemLore();
}
