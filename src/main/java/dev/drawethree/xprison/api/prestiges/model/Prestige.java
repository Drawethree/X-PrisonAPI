package dev.drawethree.xprison.api.prestiges.model;

import java.math.BigDecimal;

/**
 * Represents a Prestige level in the XPrison plugin.
 */
public interface Prestige {

    /**
     * Gets the unique identifier of this Prestige.
     *
     * @return the prestige ID
     */
    long getId();

    /**
     * Gets the cost required to reach this Prestige.
     *
     * @return the cost as a double
     * @deprecated a {@code double} cannot exactly represent OP-scale prestige costs above ~9 quadrillion
     *             (2^53); use {@link #getCostExact()} instead.
     */
    @Deprecated
    double getCost();

    /**
     * Gets the exact cost required to reach this Prestige. Prefer this over {@link #getCost()} when the
     * cost may exceed a {@code double}'s ~9 quadrillion (2^53) precision limit, so OP-scale prestige
     * costs stay exact.
     *
     * @return the exact cost of the prestige
     */
    default BigDecimal getCostExact() {
        return BigDecimal.valueOf(getCost());
    }

    /**
     * Gets the prefix associated with this Prestige (usually for display).
     *
     * @return the prefix string
     */
    String getPrefix();
}
