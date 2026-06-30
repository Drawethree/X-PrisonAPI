package dev.drawethree.xprison.api.battlepass;

import dev.drawethree.xprison.api.battlepass.model.Season;
import dev.drawethree.xprison.api.battlepass.model.XpSource;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * API interface for the Battle Pass module.
 * <p>
 * The Battle Pass is the seasonal progression meta: players earn XP from quests,
 * daily rewards and other sources, climbing tiers that grant rewards on a free and
 * a premium track. This API exposes a player's current standing and lets other
 * systems award XP or grant premium access.
 */
public interface XPrisonBattlePassAPI {

	/**
	 * Gets the currently active season, if one is configured.
	 *
	 * @return the active season, or empty if none is configured
	 */
	@NotNull
	Optional<Season> getCurrentSeason();

	/**
	 * Gets the highest tier obtainable in the current season.
	 *
	 * @return the maximum tier, or {@code 0} if no season is active
	 */
	int getMaxTier();

	/**
	 * Gets the player's current tier in the active season.
	 *
	 * @param playerUuid the player's unique id
	 * @return the player's current tier (0 if they have not earned the first tier)
	 */
	int getTier(@NotNull UUID playerUuid);

	/**
	 * Gets the player's total accumulated XP in the active season.
	 *
	 * @param playerUuid the player's unique id
	 * @return the player's total season XP
	 */
	long getTotalXp(@NotNull UUID playerUuid);

	/**
	 * Gets how much additional XP the player needs to reach their next tier.
	 *
	 * @param playerUuid the player's unique id
	 * @return the XP remaining until the next tier, or {@code 0} if the max tier is reached
	 */
	long getXpRemainingForNextTier(@NotNull UUID playerUuid);

	/**
	 * Awards Battle Pass XP to a player in the active season. Fires a cancellable
	 * {@link dev.drawethree.xprison.api.battlepass.events.BattlePassXpGainEvent} and,
	 * if the player advances, a
	 * {@link dev.drawethree.xprison.api.battlepass.events.BattlePassTierUpEvent}.
	 * Has no effect if no season is active or the amount is not positive.
	 *
	 * @param playerUuid the player's unique id
	 * @param amount     the amount of XP to award (must be positive)
	 * @param source     what caused this XP gain
	 */
	void addXp(@NotNull UUID playerUuid, long amount, @NotNull XpSource source);

	/**
	 * Checks whether the player has unlocked the premium pass for the active season.
	 *
	 * @param playerUuid the player's unique id
	 * @return {@code true} if the player has premium access
	 */
	boolean isPremium(@NotNull UUID playerUuid);

	/**
	 * Grants or revokes premium pass access for the player in the active season.
	 *
	 * @param playerUuid the player's unique id
	 * @param premium    {@code true} to grant premium access, {@code false} to revoke it
	 */
	void setPremium(@NotNull UUID playerUuid, boolean premium);

	// ---------------------------------------------------------------------
	// Administration (config + season + offline-capable player data)
	// ---------------------------------------------------------------------

	/**
	 * Reloads the Battle Pass configuration from {@code battlepass.yml} and re-applies it to
	 * online players. Use after editing the config externally (e.g. from the web dashboard).
	 */
	void reloadConfig();

	/**
	 * Switches to a new season by id: persists it, rebuilds tiers and reloads online players.
	 * Previous-season data remains archived in storage.
	 *
	 * @param seasonId the new season id
	 */
	void startNewSeason(@NotNull String seasonId);

	/**
	 * Sets a player's absolute season XP (clamped to {@code >= 0}), recomputing their tier.
	 * Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param xp         the absolute XP value to set
	 */
	void setXp(@NotNull UUID playerUuid, long xp);

	/**
	 * Clears a player's progress for the current season (XP, tier, premium and claims). Works
	 * for offline players.
	 *
	 * @param playerUuid the player's unique id
	 */
	void resetPlayer(@NotNull UUID playerUuid);
}
