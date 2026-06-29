package dev.drawethree.xprison.api.autominer.model;

/**
 * The general AutoMiner settings defined in the {@code settings:} section of
 * {@code autominer.yml}, exposed as a single editable value object.
 * <p>
 * Obtain an instance with {@code XPrisonAutoMinerAPI#getSettings()}, mutate it via the
 * setters, then persist the whole object with
 * {@code XPrisonAutoMinerAPI#saveSettings(AutoMinerSettings)}.
 */
public interface AutoMinerSettings {

    /**
     * @return the currency simulated mining income is paid in when the AutoSell module
     *         cannot provide one
     */
    String getEarningCurrency();

    /**
     * Sets the currency simulated mining income is paid in when the AutoSell module
     * cannot provide one.
     *
     * @param earningCurrency the new earning currency name
     */
    void setEarningCurrency(String earningCurrency);

    /**
     * @return whether the legacy per-region {@code rewards:} console commands still run each cycle
     */
    boolean isRunLegacyCommands();

    /**
     * Sets whether the legacy per-region {@code rewards:} console commands still run each cycle.
     *
     * @param runLegacyCommands the new value
     */
    void setRunLegacyCommands(boolean runLegacyCommands);
}
