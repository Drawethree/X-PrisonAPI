package dev.drawethree.xprison.api.pickaxequality;

import dev.drawethree.xprison.api.pickaxequality.model.PickaxeQualityTier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Optional;

/**
 * API for the Pickaxe Quality module.
 * <p>
 * Quality is a permanent, per-pickaxe tier stored in the pickaxe's NBT. Each tier multiplies what
 * the pickaxe earns while mining - currency gains, pickaxe experience and Battle Pass XP - and is
 * bought with a configurable currency. A pickaxe without the tag is tier {@code 0}, whose
 * multipliers are all exactly {@code 1.0}.
 */
public interface XPrisonPickaxeQualityAPI {

    /**
     * Gets the quality tier number stored on the given item.
     *
     * @param item the pickaxe to read (may be {@code null})
     * @return the tier number, or {@code 0} when the item is not a pickaxe or carries no tier
     */
    int getTierNumber(ItemStack item);

    /**
     * Gets the quality tier of the given item.
     *
     * @param item the pickaxe to read (may be {@code null})
     * @return the tier, never empty for a valid configuration - tier {@code 0} is the fallback
     */
    Optional<PickaxeQualityTier> getTier(ItemStack item);

    /**
     * Gets the quality tier of the pickaxe the player is currently holding.
     *
     * @param player the player whose held item is read
     * @return the tier of the held pickaxe, or empty when the player is not holding one
     */
    Optional<PickaxeQualityTier> getTier(Player player);

    /**
     * Gets a configured tier by its number.
     *
     * @param tier the tier number
     * @return the tier, or empty when no such tier is configured
     */
    Optional<PickaxeQualityTier> getTierByNumber(int tier);

    /**
     * Gets the highest configured tier.
     *
     * @return the maximum tier number, or {@code 0} when no tiers are configured
     */
    int getMaxTier();

    /**
     * Gets every configured tier, ordered by tier number.
     *
     * @return an unmodifiable collection of all tiers, tier {@code 0} included
     */
    Collection<PickaxeQualityTier> getAllTiers();

    /**
     * Sets the quality tier of an item, bypassing cost and requirement checks.
     * <p>
     * The item is not modified in place - the updated pickaxe is returned, and it is the caller's
     * responsibility to write it back into the player's inventory.
     *
     * @param player the owner of the pickaxe, used to refresh its display
     * @param item   the pickaxe to modify
     * @param tier   the tier to set, clamped to the configured range
     * @return the updated pickaxe, or the unchanged item when it is not a supported pickaxe
     */
    ItemStack setTier(Player player, ItemStack item, int tier);

    /**
     * Gets the multiplier the given pickaxe's quality applies to mining gains of a currency.
     *
     * @param item         the pickaxe to read (may be {@code null})
     * @param currencyName the currency name (e.g. {@code "tokens"}), case-insensitive
     * @return the multiplier, or {@code 1.0} when the pickaxe has no quality bonus for it
     */
    double getCurrencyMultiplier(ItemStack item, String currencyName);
}
