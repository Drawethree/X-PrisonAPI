package dev.drawethree.xprison.api;

import dev.drawethree.xprison.api.autominer.XPrisonAutoMinerAPI;
import dev.drawethree.xprison.api.autosell.XPrisonAutoSellAPI;
import dev.drawethree.xprison.api.enchants.XPrisonEnchantsAPI;
import dev.drawethree.xprison.api.gangs.XPrisonGangsAPI;
import dev.drawethree.xprison.api.gems.XPrisonGemsAPI;
import dev.drawethree.xprison.api.history.XPrisonHistoryAPI;
import dev.drawethree.xprison.api.mines.XPrisonMinesAPI;
import dev.drawethree.xprison.api.multipliers.XPrisonMultipliersAPI;
import dev.drawethree.xprison.api.pickaxelevels.XPrisonPickaxeLevelsAPI;
import dev.drawethree.xprison.api.prestiges.XPrisonPrestigesAPI;
import dev.drawethree.xprison.api.ranks.XPrisonRanksAPI;
import dev.drawethree.xprison.api.tokens.XPrisonTokensAPI;
import org.jetbrains.annotations.NotNull;

public interface XPrisonAPI {

    @NotNull
    XPrisonAutoMinerAPI getAutoMinerApi();

    @NotNull
    XPrisonAutoSellAPI getAutoSellApi();

    @NotNull
    XPrisonEnchantsAPI getEnchantsApi();

    @NotNull
    XPrisonGangsAPI getGangsApi();

    @NotNull
    XPrisonGemsAPI getGemsApi();

    @NotNull
    XPrisonHistoryAPI getHistoryApi();

    @NotNull
    XPrisonMinesAPI getMinesApi();

    @NotNull
    XPrisonMultipliersAPI getMultipliersApi();

    @NotNull
    XPrisonPickaxeLevelsAPI getPickaxeLevelsApi();

    @NotNull
    XPrisonPrestigesAPI getPrestigesApi();

    @NotNull
    XPrisonRanksAPI getRanksApi();

    @NotNull
    XPrisonTokensAPI getTokensApi();

    @NotNull
    static XPrisonAPI getInstance() {
        if (InstanceHolder.INSTANCE == null) {
            throw new IllegalStateException("X-Prison API has not been initialized!");
        }
        return InstanceHolder.INSTANCE;
    }

    static void setInstance(@NotNull XPrisonAPI instance) {
        if (InstanceHolder.INSTANCE != null) {
            throw new IllegalStateException("X-Prison API is already initialized!");
        }
        InstanceHolder.INSTANCE = instance;
    }

    class InstanceHolder {
        private static XPrisonAPI INSTANCE;
    }

}