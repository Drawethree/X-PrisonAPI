package dev.drawethree.xprison.api.milestones.model;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * One measurable statistic a milestone ladder can be built on - prestige, blocks broken, playtime,
 * or anything a plugin cares to expose.
 * <p>
 * Tracks are the extension point of the Milestones module. X-Prison registers the built-in ones on
 * startup; any plugin may register its own with
 * {@link dev.drawethree.xprison.api.milestones.registry.MilestoneRegistry#registerTrack(MilestoneTrack)},
 * after which server owners can write {@code type: <key>} in {@code milestones.yml} and build a
 * ladder on it. Entries naming a track that is not registered yet are held aside and picked up the
 * moment it registers, so registration order does not matter.
 * <p>
 * Values are {@link BigDecimal} because a track may measure a currency, and OP-scale prison
 * economies run far past what a 64-bit integer holds.
 *
 * @since 1.9
 */
public interface MilestoneTrack {

	/**
	 * Gets the key this track is written as in {@code milestones.yml} under a milestone's
	 * {@code type}. Keys are matched ignoring case and must be unique across all tracks.
	 *
	 * @return the config key, for example {@code blocks}
	 */
	@NotNull
	String getKey();

	/**
	 * Gets the name shown for this track in the menu, before any per-track override from
	 * {@code milestones.yml} is applied.
	 *
	 * @return the display name, for example {@code Blocks Broken}
	 */
	@NotNull
	String getDisplayName();

	/**
	 * Gets the one-line explanation shown for this track in the menu, before any per-track
	 * override from {@code milestones.yml} is applied.
	 *
	 * @return the description
	 */
	@NotNull
	String getDescription();

	/**
	 * Reads the player's current value on this track. Called on the server thread whenever the
	 * menu is drawn or progress is queried, so it must be cheap and must not block.
	 *
	 * @param player the player to measure
	 * @return the current value, never {@code null}; return {@link BigDecimal#ZERO} when the
	 * backing system is unavailable
	 */
	@NotNull
	BigDecimal getValue(@NotNull Player player);

	/**
	 * Checks whether this track can be measured right now. A track whose backing module or plugin
	 * is disabled should return {@code false}; it stays registered and its ladder stays
	 * configured, but the menu hides it.
	 *
	 * @return {@code true} if the track is usable
	 */
	default boolean isAvailable() {
		return true;
	}

	/**
	 * Renders a value of this track for display - a plain grouped number by default, which a
	 * track measuring something else (a duration, a currency) should override.
	 *
	 * @param value the value to render
	 * @return the text shown to the player
	 */
	@NotNull
	default String format(@NotNull BigDecimal value) {
		return new DecimalFormat("#,##0").format(value);
	}
}
