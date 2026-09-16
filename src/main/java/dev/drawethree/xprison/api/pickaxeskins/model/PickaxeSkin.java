package dev.drawethree.xprison.api.pickaxeskins.model;

import java.util.Map;

/**
 * Represents a customizable pickaxe skin used within the XPrison system.
 * Each skin has a unique ID, display name, custom model data (for resource packs),
 * and a set of multipliers that affect gameplay: currency multipliers (e.g., tokens, money, gems),
 * an enchant proc chance multiplier, a pickaxe experience multiplier and a Battle Pass XP multiplier.
 *
 * This interface provides access to all properties of a skin,
 * and allows multiplier lookups by currency name.
 */
public interface PickaxeSkin {

    /**
     * Gets the unique identifier for this skin.
     *
     * @return the internal ID of the skin (e.g., "legendary", "neon")
     */
    String getId();

    /**
     * Gets the display name of this skin, usually with color codes.
     *
     * @return the name shown to players (e.g., "§6Legendary Skin")
     */
    String getName();

    /**
     * Gets the description of this skin, usually with color codes.
     *
     * @return the description shown to players
     */
    String getDescription();

    /**
     * Gets the custom model data value used for applying the resource pack model.
     *
     * @return the CustomModelData integer for this skin
     */
    int getCustomModelData();

    /**
     * Gets the map of multipliers applied by this skin.
     * Each entry maps a currency (e.g., "tokens") to a multiplier value.
     *
     * @return a map of currency multipliers
     */
    Map<String, Double> getMultipliers();

    /**
     * Gets the permission string required to use or unlock this skin.
     *
     * @return the permission node (e.g., "pickaxeskins.skin.legendary")
     */
    String getPermission();

    /**
     * Gets the multiplier for the specified currency.
     *
     * @param currencyName the currency to get the multiplier for (e.g., "tokens", "money")
     * @return the multiplier for the currency, or 0.0 if not defined
     */
    double getMultiplier(String currencyName);

    /**
     * Gets the multiplier this skin applies to the proc chance of every chance-based enchant
     * on the pickaxe (block-break enchants and reward multipliers alike). The boosted chance is
     * capped at 100%.
     *
     * @return the proc chance multiplier, {@code 1.0} when the skin does not boost enchant procs
     * @since 1.10
     */
    default double getEnchantProcMultiplier() {
        return 1.0D;
    }

    /**
     * Gets the multiplier this skin applies to pickaxe experience earned from mining
     * (manual swings, area enchants, auto miner and any addon-registered source). Experience
     * granted through the API or admin commands is never scaled.
     *
     * @return the pickaxe experience multiplier, {@code 1.0} when the skin does not boost it
     * @since 1.10
     */
    default double getPickaxeExpMultiplier() {
        return 1.0D;
    }

    /**
     * Gets the multiplier this skin applies to Battle Pass XP earned from mining.
     *
     * @return the Battle Pass XP multiplier, {@code 1.0} when the skin does not boost it
     * @since 1.10
     */
    default double getBattlePassXpMultiplier() {
        return 1.0D;
    }
}
