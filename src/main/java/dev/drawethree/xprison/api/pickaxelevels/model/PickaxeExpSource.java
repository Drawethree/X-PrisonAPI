package dev.drawethree.xprison.api.pickaxelevels.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the source of a pickaxe experience gain.
 *
 * <p>Built-in sources are available as {@code public static final} constants. Each source can
 * have its own experience multiplier configured in {@code pickaxe-levels.yml} under the
 * {@code exp-sources} section, keyed by the lower-cased, dash-separated form of the source key
 * (e.g. {@code AREA_ENCHANTS} → {@code area-enchants}).
 *
 * <p>External addons can define and register their own sources at any time:
 * <pre>{@code
 * // In your addon — call once, store the result as a constant:
 * public static final PickaxeExpSource PET_BONUS = PickaxeExpSource.of("PET_BONUS");
 * }</pre>
 *
 * <p><b>Identity comparison ({@code ==}) is safe</b> because {@link #of(String)} is a
 * flyweight factory: the same key always returns the exact same instance.
 */
public final class PickaxeExpSource {

    private static final Map<String, PickaxeExpSource> REGISTRY = new ConcurrentHashMap<>();

    // ── Built-in sources ──────────────────────────────────────────────────────
    /** Experience gained by manually breaking a block with the pickaxe. */
    public static final PickaxeExpSource MANUAL        = of("MANUAL");

    /** Experience gained from blocks broken by area enchants (Explosive, Nuke, Layer, ...). */
    public static final PickaxeExpSource AREA_ENCHANTS = of("AREA_ENCHANTS");

    /** Experience gained from blocks mined by the AutoMiner. */
    public static final PickaxeExpSource AUTOMINER     = of("AUTOMINER");

    /** Experience granted programmatically through the XPrison API (addons, rewards, ...). */
    public static final PickaxeExpSource API           = of("API");
    // ─────────────────────────────────────────────────────────────────────────

    private final String key;

    private PickaxeExpSource(String key) {
        this.key = key;
    }

    /**
     * Returns the {@code PickaxeExpSource} for the given key, registering a new instance on
     * first use. Keys are normalised to {@code UPPER_CASE}.
     *
     * <p>This method is thread-safe and returns the same instance for equal keys, so
     * identity ({@code ==}) comparison is safe.
     *
     * @param key non-blank identifier, e.g. {@code "PET_BONUS"}
     * @return the (possibly new) {@code PickaxeExpSource} for this key
     * @throws IllegalArgumentException if {@code key} is {@code null} or blank
     */
    public static PickaxeExpSource of(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("PickaxeExpSource key must not be blank");
        }
        return REGISTRY.computeIfAbsent(key.toUpperCase(), PickaxeExpSource::new);
    }

    /**
     * Returns all registered {@code PickaxeExpSource} instances, including those added by
     * external addons. The returned collection is unmodifiable.
     */
    public static Collection<PickaxeExpSource> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    /**
     * Returns the normalised upper-case key that identifies this source,
     * e.g. {@code "MANUAL"} or {@code "PET_BONUS"}.
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
        if (!(obj instanceof PickaxeExpSource other)) return false;
        return key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
