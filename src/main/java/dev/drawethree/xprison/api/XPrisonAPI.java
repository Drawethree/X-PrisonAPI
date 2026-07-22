package dev.drawethree.xprison.api;

import dev.drawethree.xprison.api.addons.XPrisonAddonInfo;
import dev.drawethree.xprison.api.autominer.XPrisonAutoMinerAPI;
import dev.drawethree.xprison.api.autosell.XPrisonAutoSellAPI;
import dev.drawethree.xprison.api.battlepass.XPrisonBattlePassAPI;
import dev.drawethree.xprison.api.blocks.XPrisonBlocksAPI;
import dev.drawethree.xprison.api.dailyrewards.XPrisonDailyRewardsAPI;
import dev.drawethree.xprison.api.bombs.XPrisonBombsAPI;
import dev.drawethree.xprison.api.currency.XPrisonCurrencyAPI;
import dev.drawethree.xprison.api.enchants.XPrisonEnchantsAPI;
import dev.drawethree.xprison.api.gangs.XPrisonGangsAPI;
import dev.drawethree.xprison.api.history.XPrisonHistoryAPI;
import dev.drawethree.xprison.api.mines.XPrisonMinesAPI;
import dev.drawethree.xprison.api.miningstats.XPrisonMiningStatsAPI;
import dev.drawethree.xprison.api.multipliers.XPrisonMultipliersAPI;
import dev.drawethree.xprison.api.pickaxelevels.XPrisonPickaxeLevelsAPI;
import dev.drawethree.xprison.api.pickaxequality.XPrisonPickaxeQualityAPI;
import dev.drawethree.xprison.api.pickaxeskins.XPrisonPickaxeSkinsAPI;
import dev.drawethree.xprison.api.prestiges.XPrisonPrestigesAPI;
import dev.drawethree.xprison.api.quests.XPrisonQuestsAPI;
import dev.drawethree.xprison.api.ranks.XPrisonRanksAPI;
import dev.drawethree.xprison.api.text.XPrisonTextAPI;
import dev.drawethree.xprison.api.rebirth.XPrisonRebirthAPI;
import dev.drawethree.xprison.api.virtualblocks.XPrisonVirtualBlocksAPI;
import dev.drawethree.xprison.api.virtualblocks.XPrisonVirtualBlocksAPIImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
     * Gets the pickaxe quality API
     *
     * @return the Pickaxe Quality API instance
     */
    @NotNull
    XPrisonPickaxeQualityAPI getPickaxeQualityApi();

    /**
     * Gets the Rebirth API
     *
     * @return the Rebirth API instance
     */
    @NotNull
    XPrisonRebirthAPI getRebirthApi();

	/**
	 * Gets the Blocks API
	 *
	 * @return the Blocks API instance
	 */
	@NotNull
	XPrisonBlocksAPI getBlocksApi();

    /**
     * Gets the Battle Pass module API.
     *
     * @return the Battle Pass API instance
     */
    @NotNull
    XPrisonBattlePassAPI getBattlePassApi();

    /**
     * Gets the Quests module API.
     *
     * @return the Quests API instance
     */
    @NotNull
    XPrisonQuestsAPI getQuestsApi();

    /**
     * Gets the virtual (packet-only) blocks API. Packet-based private-mine plugins register their
     * {@link dev.drawethree.xprison.api.virtualblocks.VirtualBlockProvider} here so X-Prison's
     * mining pipeline can resolve and remove blocks that exist only client-side.
     *
     * @return the Virtual Blocks API instance
     */
    @NotNull
    default XPrisonVirtualBlocksAPI getVirtualBlocksApi() {
        return XPrisonVirtualBlocksAPIImpl.INSTANCE;
    }

    /**
     * Gets the Daily Rewards module API.
     *
     * @return the Daily Rewards API instance
     */
    @NotNull
    XPrisonDailyRewardsAPI getDailyRewardsApi();

    /**
     * Returns all registered modules with their current enabled state.
     */
    @NotNull
    List<XPrisonModule> getModules();

    /**
     * Enables a module by its config key and saves the state to config.yml.
     * Has no effect if the module is already enabled or the key is unknown.
     *
     * @param configKey the value of {@code @XPrisonModuleInfo(configKey)}
     */
    void enableModule(@NotNull String configKey);

    /**
     * Disables a module by its config key and saves the state to config.yml.
     * Has no effect if the module is already disabled or the key is unknown.
     *
     * @param configKey the value of {@code @XPrisonModuleInfo(configKey)}
     */
    void disableModule(@NotNull String configKey);

    /**
     * Returns metadata for every loaded addon, including whether it is currently enabled.
     */
    @NotNull
    List<XPrisonAddonInfo> getLoadedAddons();

    /**
     * Enables a loaded addon by display name.
     * Has no effect if the addon is already enabled or the name is unknown.
     *
     * @param name the addon's display name (case-insensitive)
     */
    void enableAddon(@NotNull String name);

    /**
     * Disables a loaded addon by display name.
     * Has no effect if the addon is already disabled or the name is unknown.
     *
     * @param name the addon's display name (case-insensitive)
     */
    void disableAddon(@NotNull String name);

    /**
     * Loads an addon JAR from the X-Prison addons folder at runtime.
     * The addon's {@code onEnable()} is called immediately if loading succeeds.
     *
     * @param filename the JAR filename (e.g. {@code "MyAddon.jar"}); must exist in the addons folder
     * @return {@code true} if the addon was loaded and enabled successfully
     */
    boolean loadAddonFromFile(String filename);

    /**
     * Registers the publicly accessible URL of the Dashboard addon.
     * Called by the Dashboard addon on enable; pass {@code null} on disable.
     *
     * @param url the publicly accessible URL, or {@code null} to clear
     */
    void setDashboardUrl(@Nullable String url);

    /**
     * Returns the URL registered by the Dashboard addon, or {@code null} if the addon
     * is not currently running.
     */
    @Nullable
    String getDashboardUrl();

    /**
     * Gets the text rendering API.
     *
     * <p>Use this to send and render MiniMessage text instead of depending on Adventure directly:
     * X-Prison does not shade Adventure, so an addon that imports {@code net.kyori} works on Paper
     * but fails to link on Spigot.
     *
     * @return the text API instance
     * @since 1.9
     */
    @NotNull
    XPrisonTextAPI getTextApi();

    /**
     * Returns the current version string of the X-Prison plugin.
     *
     * @return the plugin version (e.g. {@code "2026.2.2.4-BETA"})
     */
    @NotNull
    String getPluginVersion();

    /**
     * Checks whether a specific module is currently enabled.
     *
     * @param configKey the value of {@code @XPrisonModuleInfo(configKey)} for the module
     * @return {@code true} if the module is loaded and enabled
     */
    boolean isModuleEnabled(@NotNull String configKey);

    /**
     * Checks whether X-Prison's UltraBackpacks integration is active.
     * <p>
     * When it is, mined drops belong in the player's backpack rather than their inventory, so any
     * enchant that hands out drops itself must route them through UltraBackpacks instead. Note that
     * UltraBackpacks reads real world block state and cannot resolve virtual (packet-mine) blocks,
     * so callers should bypass it whenever the affected blocks are virtual.
     *
     * @return {@code true} if the UltraBackpacks integration is enabled and usable
     * @since 1.9
     */
    default boolean isUltraBackpacksEnabled() {
        return false;
    }

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
