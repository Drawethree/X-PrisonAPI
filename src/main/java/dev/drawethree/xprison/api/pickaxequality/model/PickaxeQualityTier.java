package dev.drawethree.xprison.api.pickaxequality.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * A single Pickaxe Quality tier.
 * <p>
 * Quality is a permanent, per-pickaxe upgrade stored on the pickaxe itself. Every pickaxe starts at
 * tier {@code 0}, whose multipliers are all exactly {@code 1.0}; buying a tier permanently scales
 * what that pickaxe earns while mining.
 */
public interface PickaxeQualityTier {

    /**
     * Gets the tier number. Tier {@code 0} is the implicit starting tier every pickaxe has.
     *
     * @return the tier number, {@code 0} or greater
     */
    int getTier();

    /**
     * Gets the display name of this tier, in raw (unrendered) config form.
     *
     * @return the display name shown to players
     */
    String getDisplayName();

    /**
     * Gets every currency multiplier defined by this tier, keyed by currency name.
     *
     * @return an unmodifiable map of currency name to multiplier
     */
    Map<String, Double> getCurrencyMultipliers();

    /**
     * Gets the multiplier this tier applies to mining gains of the given currency.
     *
     * @param currencyName the currency name (e.g. {@code "tokens"}), case-insensitive
     * @return the multiplier, or {@code 1.0} when this tier does not modify that currency
     */
    double getCurrencyMultiplier(String currencyName);

    /**
     * Gets the multiplier this tier applies to pickaxe experience gains.
     *
     * @return the pickaxe experience multiplier, {@code 1.0} when unmodified
     */
    double getPickaxeExpMultiplier();

    /**
     * Gets the multiplier this tier applies to Battle Pass XP gained from mining.
     *
     * @return the Battle Pass XP multiplier, {@code 1.0} when unmodified
     */
    double getBattlePassXpMultiplier();

    /**
     * Gets the name of the currency this tier is bought with.
     *
     * @return the cost currency name, or {@code null} for tier {@code 0} which cannot be bought
     */
    String getCostCurrency();

    /**
     * Gets the exact price of this tier. Exact rather than {@code double} so OP-scale prices
     * (10^24 and beyond) stay lossless.
     *
     * @return the cost, or {@link BigDecimal#ZERO} for tier {@code 0}
     */
    BigDecimal getCost();

    /**
     * Gets the pickaxe level required before this tier can be bought.
     *
     * @return the required pickaxe level, or {@code 0} when there is no requirement
     */
    int getRequiredPickaxeLevel();

    /**
     * Gets the permission required to buy this tier.
     *
     * @return the permission node, or {@code null} when the tier is unrestricted
     */
    String getPermission();

    /**
     * Gets the console commands executed when a player reaches this tier.
     *
     * @return an unmodifiable list of commands, {@code %player%} being the player's name
     */
    List<String> getRewards();
}
