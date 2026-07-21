package dev.drawethree.xprison.api.enchants.area;

import com.google.gson.JsonObject;
import dev.drawethree.xprison.api.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;

/**
 * The configuration shared by every area-break enchant.
 *
 * @param countBlocksBroken whether the break advances blocks/pickaxe statistics and fires X-Prison's
 *                          aggregate break events
 * @param eventStrategy     how the break is announced to the rest of the server
 * @param currencyToGive    name of the currency auto-sell earnings are paid into
 * @param message           proc message supporting {@code %amount%} and {@code %currency%}; empty disables it
 * @param maxBlocks         hard cap on blocks affected per proc; {@code 0} means no cap
 * @param removeBlocks      whether the affected blocks are actually cleared; {@code false} makes the
 *                          enchant <b>reward-only</b> — it pays for the blocks but leaves them
 *                          standing, touching no block state and sending no update packets, which is
 *                          the cheapest mode an area enchant can run in
 * @since 1.9
 */
public record AreaBreakSettings(boolean countBlocksBroken,
                                @NotNull BreakEventStrategy eventStrategy,
                                @NotNull String currencyToGive,
                                @NotNull String message,
                                int maxBlocks,
                                boolean removeBlocks) {

	/**
	 * Enforces the one combination that cannot be meaningful: a <b>reward-only</b> enchant
	 * ({@code removeBlocks = false}) always uses {@link BreakEventStrategy#NONE}.
	 * <p>
	 * Nothing is destroyed in that mode, so announcing breaks would be a lie with real consequences —
	 * a mine plugin listening for those events would count the untouched blocks toward its reset and
	 * wipe a mine that is still full. Any configured strategy is therefore overridden here rather
	 * than at one call site, so no construction path can produce the inconsistent pairing.
	 */
	public AreaBreakSettings {
		if (!removeBlocks) {
			eventStrategy = BreakEventStrategy.NONE;
		}
	}

	/**
	 * Reads the shared settings from an enchant's JSON.
	 * <p>
	 * The modern {@code eventStrategy} key wins; when it is absent the legacy boolean
	 * {@code useEvents} is honoured so existing configurations keep their behaviour, and when
	 * neither is present {@code defaultStrategy} applies.
	 *
	 * @param config          the enchantment's JSON configuration
	 * @param defaultStrategy the strategy to use when the config specifies none
	 * @return the parsed settings
	 */
	/**
	 * Returns a copy using a different proc message.
	 * <p>
	 * Useful for enchants whose JSON separates an announcement message from the earnings message —
	 * the pipeline's message is the earnings one, so such an enchant remaps it after the shared
	 * settings are parsed.
	 *
	 * @param message the earnings message to use
	 * @return a copy with the message replaced
	 */
	@NotNull
	public AreaBreakSettings withMessage(@NotNull String message) {
		return new AreaBreakSettings(countBlocksBroken, eventStrategy, currencyToGive, message, maxBlocks, removeBlocks);
	}

	@NotNull
	public static AreaBreakSettings fromJson(@NotNull JsonObject config, @NotNull BreakEventStrategy defaultStrategy) {
		BreakEventStrategy strategy;
		if (config.has("eventStrategy")) {
			strategy = BreakEventStrategy.parse(JsonUtils.getOptionalString(config, "eventStrategy", null), defaultStrategy);
		} else if (config.has("useEvents")) {
			strategy = BreakEventStrategy.fromLegacyUseEvents(JsonUtils.getOptionalBoolean(config, "useEvents", true));
		} else {
			strategy = defaultStrategy;
		}

		return new AreaBreakSettings(
				JsonUtils.getOptionalBoolean(config, "countBlocksBroken", false),
				strategy,
				JsonUtils.getOptionalString(config, "currencyToGive", "money"),
				JsonUtils.getOptionalString(config, "message", ""),
				Math.max(0, JsonUtils.getOptionalInt(config, "maxBlocks", 0)),
				// Defaults to true so enchants that have always cleared keep doing so.
				JsonUtils.getOptionalBoolean(config, "removeBlocks", true));
	}
}
