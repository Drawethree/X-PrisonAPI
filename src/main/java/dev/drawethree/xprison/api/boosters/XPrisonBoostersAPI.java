package dev.drawethree.xprison.api.boosters;

import dev.drawethree.xprison.api.boosters.model.Booster;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Access to the Boosters module: personal, queued, time-limited multipliers for currency income and
 * Battle Pass XP, usually sold as items through a webstore.
 * <p>
 * A player can hold several boosters for the same target; they run one after another and only count
 * down while the player is online. Booster data of online players is kept in memory, so every
 * method here is cheap.
 *
 * @since 1.10
 */
public interface XPrisonBoostersAPI {

	/**
	 * Gets the multiplier of the player's running booster for a target.
	 *
	 * @param player the player
	 * @param target a currency name or {@link Booster#BATTLE_PASS_XP}
	 * @return the multiplier, or {@code 1.0} if no booster for that target is running
	 */
	double getActiveMultiplier(@NotNull UUID player, @NotNull String target);

	/**
	 * Gets every booster of an online player, running ones first, then the queue in order.
	 *
	 * @param player the player
	 * @return the boosters; empty if the player has none or is offline
	 */
	@NotNull
	List<Booster> getBoosters(@NotNull UUID player);

	/**
	 * Adds a booster to the end of a player's queue for its target. It starts at once if nothing
	 * else for that target is running. Works for offline players too; their booster waits until
	 * they join.
	 *
	 * @param player          the player
	 * @param target          a currency name or {@link Booster#BATTLE_PASS_XP}
	 * @param multiplier      the multiplier, at least {@code 1.0}
	 * @param durationSeconds how long it runs, in seconds of online time
	 * @param source          a label for where it came from, shown to admins
	 * @return {@code true} if it was queued, {@code false} if the queue for that target is full or
	 * the values are invalid
	 */
	boolean queueBooster(@NotNull UUID player, @NotNull String target, double multiplier, long durationSeconds, @NotNull String source);
}
