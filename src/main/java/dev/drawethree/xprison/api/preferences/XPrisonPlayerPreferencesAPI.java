package dev.drawethree.xprison.api.preferences;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Per-player on/off switches, stored in the database and surfaced in-game through {@code /toggles}.
 *
 * <p>X-Prison ships three of them - {@link #AUTO_RANKUP}, {@link #AUTO_PRESTIGE} and
 * {@link #AUTO_REBIRTH} - and any module or addon may store its own switch here under its own key.
 * A key is an arbitrary string; prefix your own with your addon's name to avoid collisions.
 *
 * <p>A preference has three states, not two: on, off, and never set. A player who has never opened
 * {@code /toggles} has no stored value, which is why every read takes an explicit fallback rather
 * than defaulting to {@code false}. Pass the server-wide default for that feature.
 *
 * <p><strong>A server switch still wins.</strong> These preferences decide whether a feature
 * applies to a player, not whether it exists. Auto-rebirth does nothing while
 * {@code auto-rebirth} is {@code false} in {@code rebirths.yml}, whatever this returns.
 *
 * <h2>Threading</h2>
 * Reads for an <em>online</em> player are served from an in-memory cache and are safe anywhere.
 * Reads for an <em>offline</em> player hit the database and block - call
 * {@link #getPreferencesAsync(UUID)} instead, or run them off the main thread yourself. Writes
 * never block: they update the cache when the player is online and persist asynchronously.
 *
 * @since 1.9
 */
public interface XPrisonPlayerPreferencesAPI {

    /**
     * Preference key for automatically ranking a player up as soon as they can afford it.
     * Requires {@code auto-rank-up} in {@code ranks.yml}; the per-player default is
     * {@code auto-rank-up-default}.
     */
    String AUTO_RANKUP = "auto-rankup";

    /**
     * Preference key for automatically prestiging a player as soon as they qualify.
     * Requires {@code auto-prestige} in {@code prestiges.yml}; the per-player default is
     * {@code auto-prestige-default}.
     */
    String AUTO_PRESTIGE = "auto-prestige";

    /**
     * Preference key for automatically rebirthing a player as soon as they meet every requirement.
     * Requires {@code auto-rebirth} in {@code rebirths.yml}; the per-player default is
     * {@code auto-rebirth-default}.
     */
    String AUTO_REBIRTH = "auto-rebirth";

    /**
     * Reads one preference, falling back when the player has never set it.
     *
     * <p>Works for offline players; see the threading note on the interface.
     *
     * @param playerUuid the player
     * @param key        the preference key, e.g. {@link #AUTO_REBIRTH}
     * @param fallback   what to return when this player has no stored value for the key
     * @return the stored value, or {@code fallback}
     */
    boolean isEnabled(@NotNull UUID playerUuid, @NotNull String key, boolean fallback);

    /**
     * Writes one preference.
     *
     * <p>Works for offline players: the value is persisted and picked up the next time they log in.
     * Does not block - the database write happens asynchronously.
     *
     * @param playerUuid the player
     * @param key        the preference key, e.g. {@link #AUTO_REBIRTH}
     * @param enabled    the new value
     */
    void set(@NotNull UUID playerUuid, @NotNull String key, boolean enabled);

    /**
     * Reads every preference this player has explicitly set.
     *
     * <p>Keys the player has never touched are absent rather than {@code false}, so an empty map
     * means "has never opened {@code /toggles}", not "has turned everything off".
     *
     * <p>Works for offline players; see the threading note on the interface.
     *
     * @param playerUuid the player
     * @return an immutable snapshot of the player's stored preferences
     */
    @NotNull
    Map<String, Boolean> getPreferences(@NotNull UUID playerUuid);

    /**
     * Same as {@link #getPreferences(UUID)}, off the calling thread.
     *
     * <p>Prefer this whenever the player may be offline, since that read goes to the database.
     *
     * @param playerUuid the player
     * @return a future completing with the player's stored preferences
     */
    @NotNull
    default CompletableFuture<Map<String, Boolean>> getPreferencesAsync(@NotNull UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> getPreferences(playerUuid));
    }
}
