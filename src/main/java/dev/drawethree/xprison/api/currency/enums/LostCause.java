package dev.drawethree.xprison.api.currency.enums;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the reason why a player lost currency.
 *
 * <p>Built-in causes are available as {@code public static final} constants — all existing
 * code using {@code LostCause.ENCHANT}, {@code LostCause.RANKUP}, etc. compiles and
 * behaves identically to before.
 *
 * <p>External addons can define and register their own causes at any time:
 * <pre>{@code
 * // In your addon — call once, store the result as a constant:
 * public static final LostCause COINFLIP_WAGER = LostCause.of("COINFLIP_WAGER");
 * }</pre>
 *
 * <p><b>Identity comparison ({@code ==}) still works</b> because {@link #of(String)} is a
 * flyweight factory: the same key always returns the exact same instance.
 *
 * <p><b>Migration notes for code that used the old enum:</b>
 * <ul>
 *   <li>{@code LostCause.ENCHANT} — unchanged.
 *   <li>{@code LostCause.valueOf("ENCHANT")} → {@code LostCause.of("ENCHANT")}.
 *   <li>{@code LostCause.values()} — still available; now includes addon-registered causes.
 *   <li>{@code switch (cause)} on the enum type → {@code switch (cause.getKey())}.
 * </ul>
 */
public final class LostCause {

    private static final Map<String, LostCause> REGISTRY = new ConcurrentHashMap<>();

    // ── Built-in causes (mirrors original enum values) ───────────────────────
    /** Player lost currency via the /pay command. */
    public static final LostCause PAY      = of("PAY");

    /** Player lost currency by enchanting items. */
    public static final LostCause ENCHANT  = of("ENCHANT");

    /** Player lost currency by prestiging. */
    public static final LostCause PRESTIGE = of("PRESTIGE");

    /** Player lost currency by ranking up. */
    public static final LostCause RANKUP   = of("RANKUP");

    /** Player lost currency by rebirthing. */
    public static final LostCause REBIRTH  = of("REBIRTH");

    /** Currency removed by an admin command. */
    public static final LostCause ADMIN    = of("ADMIN");

    /** Player lost currency by withdrawing it to physical item form. */
    public static final LostCause WITHDRAW = of("WITHDRAW");

    /** Reason not specified or not covered by another constant. */
    public static final LostCause UNKNOWN  = of("UNKNOWN");
    // ─────────────────────────────────────────────────────────────────────────

    private final String key;

    private LostCause(String key) {
        this.key = key;
    }

    /**
     * Returns the {@code LostCause} for the given key, registering a new instance on
     * first use. Keys are normalised to {@code UPPER_CASE}.
     *
     * <p>This method is thread-safe and returns the same instance for equal keys, so
     * identity ({@code ==}) comparison is safe.
     *
     * @param key non-blank identifier, e.g. {@code "COINFLIP_WAGER"}
     * @return the (possibly new) {@code LostCause} for this key
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     */
    public static LostCause of(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("LostCause key must not be blank");
        }
        return REGISTRY.computeIfAbsent(key.toUpperCase(), LostCause::new);
    }

    /**
     * Returns all registered {@code LostCause} instances, including those added by
     * external addons. The returned collection is unmodifiable.
     */
    public static Collection<LostCause> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    /**
     * Returns the normalised upper-case key that identifies this cause,
     * e.g. {@code "ENCHANT"} or {@code "COINFLIP_WAGER"}.
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
        if (!(obj instanceof LostCause other)) return false;
        return key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
