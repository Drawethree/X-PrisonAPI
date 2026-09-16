package dev.drawethree.xprison.api.time;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Server-wide time settings shared by X-Prison and its addons.
 *
 * <p>Everything time-related in X-Prison is evaluated against one configured time zone
 * ({@code time.timezone} in {@code config.yml}): daily reward and quest resets, Battle Pass
 * season bounds, timestamps shown in menus and the plugin's own log files. Durations, dates
 * and time-unit names rendered for players use the pattern and labels from the same section.
 *
 * <p>Addons that show countdowns, cooldowns or timestamps should render them through this API,
 * so a server that reads {@code "5 min"} in one menu never reads {@code "5m"} in another and a
 * translated server never leaks an English {@code "Minutes"}.
 *
 * <p>All methods are safe to call from any thread and reflect the current configuration
 * after {@code /xprison reload}.
 *
 * @since 1.10
 */
public interface XPrisonTimeAPI {

    /**
     * Returns the time zone every date in X-Prison is evaluated in.
     *
     * @return the configured zone, or the server's system zone when none is configured
     */
    @NotNull
    ZoneId getZone();

    /**
     * Returns the current wall-clock time in the configured zone.
     *
     * @return the current zoned date-time
     */
    @NotNull
    ZonedDateTime now();

    /**
     * Renders a duration with the configured short unit labels, for example {@code 1d 4h 20m 5s}.
     *
     * <p>Units that are zero are omitted. A zero or negative duration renders as zero seconds
     * ({@code 0s} with the default labels).
     *
     * @param seconds the duration in seconds
     * @return the rendered duration
     */
    @NotNull
    String formatDuration(long seconds);

    /**
     * Renders an instant with the configured date pattern in the configured zone.
     *
     * @param instant the instant to render
     * @return the rendered date and time
     */
    @NotNull
    String formatDateTime(@NotNull Instant instant);

    /**
     * Returns the configured display name of a time unit, for example {@code Minutes}.
     *
     * @param unit the unit
     * @return the configured name, or the capitalised unit name when none is configured for it
     */
    @NotNull
    String getUnitName(@NotNull TimeUnit unit);

    /**
     * Renders an amount together with its configured unit name, for example {@code 5 Minutes}.
     *
     * @param amount the amount of units
     * @param unit   the unit
     * @return the rendered amount
     */
    @NotNull
    String formatAmount(long amount, @NotNull TimeUnit unit);
}
