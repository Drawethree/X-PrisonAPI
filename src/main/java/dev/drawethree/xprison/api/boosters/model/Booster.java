package dev.drawethree.xprison.api.boosters.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A personal booster owned by a player - either running or waiting in the player's queue.
 * <p>
 * Boosters with the same target run one after another: only the first one in the queue is active
 * and counts down, the rest wait. A booster only counts down while its owner is online.
 *
 * @since 1.10
 */
public interface Booster {

	/**
	 * The target every booster for currency income uses is the currency name, for example
	 * {@code "tokens"}. Boosters for Battle Pass XP use this target instead.
	 */
	String BATTLE_PASS_XP = "battlepass-xp";

	/**
	 * Gets the unique id of this booster.
	 *
	 * @return the booster id
	 */
	@NotNull
	UUID getUid();

	/**
	 * Gets the player who owns this booster.
	 *
	 * @return the owner's UUID
	 */
	@NotNull
	UUID getOwner();

	/**
	 * Gets what this booster boosts: a currency name or {@link #BATTLE_PASS_XP}.
	 *
	 * @return the target
	 */
	@NotNull
	String getTarget();

	/**
	 * Gets the multiplier this booster applies while active.
	 *
	 * @return the multiplier, for example {@code 2.0}
	 */
	double getMultiplier();

	/**
	 * Gets how long this booster still runs. For a queued booster this is its full duration.
	 *
	 * @return the remaining time in seconds
	 */
	long getRemainingSeconds();

	/**
	 * Gets where this booster came from, for example the booster id of a redeemed item or
	 * {@code "command"}.
	 *
	 * @return the source label
	 */
	@NotNull
	String getSource();
}
