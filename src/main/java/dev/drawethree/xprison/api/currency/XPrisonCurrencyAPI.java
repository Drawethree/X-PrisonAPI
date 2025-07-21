package dev.drawethree.xprison.api.currency;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;

import java.util.Collection;

/**
 * API for interacting with XPrison's currency system.
 * <p>
 * Allows external plugins to register new currencies or retrieve existing ones.
 */
public interface XPrisonCurrencyAPI {

    /**
     * Registers a new custom currency to the system.
     * <p>
     * If a currency with the same name (case-insensitive) already exists, it will be overwritten.
     *
     * @param currency The {@link XPrisonCurrency} implementation to register.
     */
    void registerCurrency(XPrisonCurrency currency);

    /**
     * Retrieves a currency by its name.
     *
     * @param name The name of the currency (case-insensitive).
     * @return The matching {@link XPrisonCurrency}, or null if not found.
     */
    XPrisonCurrency getCurrency(String name);

    /**
     * Returns all registered currencies.
     *
     * @return A collection of all {@link XPrisonCurrency} objects.
     */
    Collection<XPrisonCurrency> getAllCurrencies();
}
