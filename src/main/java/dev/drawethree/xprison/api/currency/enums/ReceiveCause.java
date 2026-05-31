package dev.drawethree.xprison.api.currency.enums;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the reason why a player received currency.
 *
 * <p>Built-in causes are available as {@code public static final} constants — all existing
 * code using {@code ReceiveCause.MINING}, {@code ReceiveCause.REFUND}, etc. compiles and
 * behaves identically to before.
 *
 * <p>External addons can define and register their own causes at any time:
 * <pre>{@code
 * // In your addon — call once, store the result as a constant:
 * public static final ReceiveCause COINFLIP_WIN = ReceiveCause.of("COINFLIP_WIN");
 * }</pre>
 *
 * <p><b>Identity comparison ({@code ==}) still works</b> because {@link #of(String)} is a
 * flyweight factory: the same key always returns the exact same instance.
 *
 * <p><b>Migration notes for code that used the old enum:</b>
 * <ul>
 *   <li>{@code ReceiveCause.REFUND} — unchanged.
 *   <li>{@code ReceiveCause.valueOf("REFUND")} → {@code ReceiveCause.of("REFUND")}.
 *   <li>{@code ReceiveCause.values()} — still available; now includes addon-registered causes.
 *   <li>{@code switch (cause)} on the enum type → {@code switch (cause.getKey())}.
 * </ul>
 */
public final class ReceiveCause {

    private static final Map<String, ReceiveCause> REGISTRY = new ConcurrentHashMap<>();

    // ── Built-in causes (mirrors original enum values) ───────────────────────
    /** Player received currency by mining (their own blocks). */
    public static final ReceiveCause MINING        = of("MINING");

    /** Player received currency via the /pay command. */
    public static final ReceiveCause PAY           = of("PAY");

    /** Player received currency via an admin give command. */
    public static final ReceiveCause GIVE          = of("GIVE");

    /** Player received currency by redeeming physical currency items. */
    public static final ReceiveCause REDEEM        = of("REDEEM");

    /** Player received currency from a lucky block. */
    public static final ReceiveCause LUCKY_BLOCK   = of("LUCKY_BLOCK");

    /** Currency refunded to the player (e.g. disenchant refund). */
    public static final ReceiveCause REFUND        = of("REFUND");

    /** Player received currency from another player's mining (e.g. Charity enchant). */
    public static final ReceiveCause MINING_OTHERS = of("MINING_OTHERS");

    /** Reason not specified or not covered by another constant. */
    public static final ReceiveCause UNKNOWN       = of("UNKNOWN");
    // ─────────────────────────────────────────────────────────────────────────

    private final String key;

    private ReceiveCause(String key) {
        this.key = key;
    }

    /**
     * Returns the {@code ReceiveCause} for the given key, registering a new instance on
     * first use. Keys are normalised to {@code UPPER_CASE}.
     *
     * <p>This method is thread-safe and returns the same instance for equal keys, so
     * identity ({@code ==}) comparison is safe.
     *
     * @param key non-blank identifier, e.g. {@code "COINFLIP_WIN"}
     * @return the (possibly new) {@code ReceiveCause} for this key
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     */
    public static ReceiveCause of(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("ReceiveCause key must not be blank");
        }
        return REGISTRY.computeIfAbsent(key.toUpperCase(), ReceiveCause::new);
    }

    /**
     * Returns all registered {@code ReceiveCause} instances, including those added by
     * external addons. The returned collection is unmodifiable.
     */
    public static Collection<ReceiveCause> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    /**
     * Returns the normalised upper-case key that identifies this cause,
     * e.g. {@code "MINING"} or {@code "COINFLIP_WIN"}.
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReceiveCause other)) return false;
        return key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
