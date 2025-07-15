package dev.drawethree.xprison.api.currency;

/**
 * Represents the different types of currencies supported by the XPrison plugin.
 * These currencies can be used for various in-game transactions, upgrades, and features.
 */
public enum CurrencyType {

    /**
     * Gems currency, typically used for high-tier or cosmetic purchases.
     */
    GEMS,

    /**
     * Tokens currency, often used for enchanting, upgrades, or mid-tier trades.
     */
    TOKENS,

    /**
     * Vault currency, representing stored or secured in-game balance.
     */
    VAULT
}
