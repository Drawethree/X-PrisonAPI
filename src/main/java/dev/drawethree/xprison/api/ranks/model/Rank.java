package dev.drawethree.xprison.api.ranks.model;

import java.math.BigDecimal;

/**
 * Represents a rank in the XPrison system.
 */
public interface Rank {

    /**
     * Gets the unique identifier of this rank.
     *
     * @return the rank ID
     */
    int getId();

    /**
     * Gets the cost required to obtain or rank up to this rank.
     *
     * @return the cost of the rank
     * @deprecated a {@code double} cannot exactly represent OP-scale rank costs above ~9 quadrillion
     *             (2^53); use {@link #getCostExact()} instead.
     */
    @Deprecated
    double getCost();

    /**
     * Gets the exact cost required to obtain or rank up to this rank. Prefer this over
     * {@link #getCost()} when the cost may exceed a {@code double}'s ~9 quadrillion (2^53) precision
     * limit, so OP-scale rank costs stay exact.
     *
     * @return the exact cost of the rank
     */
    default BigDecimal getCostExact() {
        return BigDecimal.valueOf(getCost());
    }

    /**
     * Gets the prefix string associated with this rank,
     * typically used for display in chat or player lists.
     *
     * @return the prefix of the rank
     */
    String getPrefix();
}
