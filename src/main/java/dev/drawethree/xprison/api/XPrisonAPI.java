package dev.drawethree.xprison.api;

import dev.drawethree.xprison.api.autominer.XPrisonAutoMinerAPI;
import dev.drawethree.xprison.api.autosell.XPrisonAutoSellAPI;
import dev.drawethree.xprison.api.bombs.XPrisonBombsAPI;
import dev.drawethree.xprison.api.currency.XPrisonCurrencyAPI;
import dev.drawethree.xprison.api.enchants.XPrisonEnchantsAPI;
import dev.drawethree.xprison.api.gangs.XPrisonGangsAPI;
import dev.drawethree.xprison.api.history.XPrisonHistoryAPI;
import dev.drawethree.xprison.api.mines.XPrisonMinesAPI;
import dev.drawethree.xprison.api.miningstats.XPrisonMiningStatsAPI;
import dev.drawethree.xprison.api.multipliers.XPrisonMultipliersAPI;
import dev.drawethree.xprison.api.pickaxelevels.XPrisonPickaxeLevelsAPI;
import dev.drawethree.xprison.api.pickaxeskins.XPrisonPickaxeSkinsAPI;
import dev.drawethree.xprison.api.prestiges.XPrisonPrestigesAPI;
import dev.drawethree.xprison.api.ranks.XPrisonRanksAPI;
import dev.drawethree.xprison.api.rebirth.XPrisonRebirthAPI;
import org.jetbrains.annotations.NotNull;

/**
 * Main API interface for interacting with the X-Prison plugin modules.
 */
public interface XPrisonAPI {

    /**
     * Gets the AutoMiner module API.
     *
     * @return the AutoMiner API instance
     */
    @NotNull
    XPrisonAutoMinerAPI getAutoMinerApi();

    /**
     * Gets the AutoSell module API.
     *
     * @return the AutoSell API instance
     */
    @NotNull
    XPrisonAutoSellAPI getAutoSellApi();

    /**
     * Gets the Enchants module API.
     *
     * @return the Enchants API instance
     */
    @NotNull
    XPrisonEnchantsAPI getEnchantsApi();

    /**
     * Gets the Gangs module API.
     *
     * @return the Gangs API instance
     */
    @NotNull
    XPrisonGangsAPI getGangsApi();

    /**
     * Gets the History module API.
     *
     * @return the History API instance
     */
    @NotNull
    XPrisonHistoryAPI getHistoryApi();

    /**
     * Gets the Mines module API.
     *
     * @return the Mines API instance
     */
    @NotNull
    XPrisonMinesAPI getMinesApi();

    /**
     * Gets the Multipliers module API.
     *
     * @return the Multipliers API instance
     */
    @NotNull
    XPrisonMultipliersAPI getMultipliersApi();

    /**
     * Gets the Pickaxe Levels module API.
     *
     * @return the Pickaxe Levels API instance
     */
    @NotNull
    XPrisonPickaxeLevelsAPI getPickaxeLevelsApi();

    /**
     * Gets the Prestiges module API.
     *
     * @return the Prestiges API instance
     */
    @NotNull
    XPrisonPrestigesAPI getPrestigesApi();

    /**
     * Gets the Ranks module API.
     *
     * @return the Ranks API instance
     */
    @NotNull
    XPrisonRanksAPI getRanksApi();

    /**
     * Gets the Bombs module API.
     *
     * @return the Bombs API instance
     */
    @NotNull
    XPrisonBombsAPI getBombsApi();

    /**
     * Gets the currency API.
     *
     * @return the Currency API instance
     */
    @NotNull
    XPrisonCurrencyAPI getCurrencyApi();

    /**
     * Gets the mining stats API.
     *
     * @return the Mining Stats API instance
     */
    @NotNull
    XPrisonMiningStatsAPI getMiningStatsApi();

    /**
     * Gets the pickaxe skins API
     *
     * @return the Pickaxe Skins API instance
     */
    @NotNull
    XPrisonPickaxeSkinsAPI getPickaxeSkinsApi();

    /**
     * Gets the Rebirth API
     *
     * @return the Rebirth API instance
     */
    @NotNull
    XPrisonRebirthAPI getRebirthApi();

    /**
     * Gets the singleton instance of the XPrisonAPI.
     *
     * @return the XPrisonAPI instance
     * @throws IllegalStateException if the API has not been initialized
     */
    @NotNull
    static XPrisonAPI getInstance() {
        if (InstanceHolder.INSTANCE == null) {
            throw new IllegalStateException("X-Prison API has not been initialized!");
        }
        return InstanceHolder.INSTANCE;
    }

    /**
     * Sets the singleton instance of the XPrisonAPI.
     *
     * @param instance the API instance to set
     * @throws IllegalStateException if the API is already initialized
     */
    static void setInstance(@NotNull XPrisonAPI instance) {
        if (InstanceHolder.INSTANCE != null) {
            throw new IllegalStateException("X-Prison API is already initialized!");
        }
        InstanceHolder.INSTANCE = instance;
    }

    /**
     * Holder class for the singleton instance.
     */
    class InstanceHolder {
        private static XPrisonAPI INSTANCE;
    }

}
