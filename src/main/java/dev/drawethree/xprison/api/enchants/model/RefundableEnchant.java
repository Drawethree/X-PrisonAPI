package dev.drawethree.xprison.api.enchants.model;

/**
 * Interface representing enchantments that support refunding mechanics.
 */
public interface RefundableEnchant {

    /**
     * Indicates whether refunding this enchantment is enabled.
     *
     * @return true if refunding is enabled, false otherwise.
     */
    boolean isRefundEnabled();

    /**
     * Gets the GUI slot position where this enchantment's refund option appears
     * in the disenchanting GUI.
     *
     * @return the slot index in the disenchant GUI.
     */
    int getRefundGuiSlot();

    /**
     * Gets the page of the disenchanting GUI this enchantment's refund option appears on.
     * <p>
     * Pages are 1-based, so {@code 1} is the first page. Enchantments that do not fit the
     * page they ask for - because the slot is already taken, or falls outside the menu's
     * configured content region - are moved to the next free slot automatically, spilling
     * onto additional pages as needed.
     *
     * @return the 1-based disenchant GUI page; defaults to {@code 1}.
     * @since 1.9
     */
    default int getRefundGuiPage() {
        return 1;
    }

    /**
     * Gets the percentage of the original enchantment price that will be refunded
     * upon disenchanting.
     * Value ranges between 0.00 (0%) and 100.00 (100%).
     *
     * @return the refund percentage.
     */
    double getRefundPercentage();
}
