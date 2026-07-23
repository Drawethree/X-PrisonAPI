package dev.drawethree.xprison.api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Small helpers for exact-decimal money math shared across the API surface (enchant pricing,
 * enchant events). Kept dependency-free so external add-ons that only ship against the API jar
 * can rely on it.
 */
public final class BigNumbers {

    private static final BigDecimal LONG_MAX = BigDecimal.valueOf(Long.MAX_VALUE);
    private static final BigDecimal LONG_MIN = BigDecimal.valueOf(Long.MIN_VALUE);

    private BigNumbers() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    /** A finite {@link BigDecimal} for {@code value}, or {@link BigDecimal#ZERO} for NaN/Infinity. */
    public static BigDecimal finite(double value) {
        return Double.isFinite(value) ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
    }

    /**
     * Saturating conversion to {@code long}: values beyond the {@code long} range clamp to
     * {@link Long#MAX_VALUE} / {@link Long#MIN_VALUE} instead of wrapping negative. Fractions round
     * half-up, matching the {@link Math#round(double)} the legacy {@code long} pricing used.
     */
    public static long clampToLong(BigDecimal value) {
        if (value == null) {
            return 0L;
        }
        if (value.compareTo(LONG_MAX) >= 0) {
            return Long.MAX_VALUE;
        }
        if (value.compareTo(LONG_MIN) <= 0) {
            return Long.MIN_VALUE;
        }
        return value.setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
