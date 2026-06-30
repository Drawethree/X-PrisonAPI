package dev.drawethree.xprison.api.quests;

import dev.drawethree.xprison.api.quests.model.ActiveQuest;
import dev.drawethree.xprison.api.quests.model.Quest;
import dev.drawethree.xprison.api.quests.model.QuestCategory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * API interface for the Quests module.
 * <p>
 * Quests turn ordinary gameplay (mining, selling, prestiging, ...) into rewarded
 * objectives. Daily and weekly quests rotate from configurable pools; permanent quests
 * are always active. This API exposes a player's active quests and lets integrations read
 * or push progress.
 */
public interface XPrisonQuestsAPI {

	/**
	 * Gets the player's currently active quests for a category.
	 *
	 * @param playerUuid the player's unique id
	 * @param category   the category to query
	 * @return an immutable list of the player's active quests in that category
	 */
	@NotNull
	List<ActiveQuest> getActiveQuests(@NotNull UUID playerUuid, @NotNull QuestCategory category);

	/**
	 * Looks up a configured quest definition by id.
	 *
	 * @param questId the quest id
	 * @return the quest, or empty if no quest with that id is configured
	 */
	@NotNull
	Optional<Quest> getQuestById(@NotNull String questId);

	/**
	 * Gets the player's progress on a specific active quest.
	 *
	 * @param playerUuid the player's unique id
	 * @param questId    the quest id
	 * @return the current progress, or {@code 0} if the quest is not active for the player
	 */
	long getProgress(@NotNull UUID playerUuid, @NotNull String questId);

	/**
	 * Checks whether the player has completed a specific active quest.
	 *
	 * @param playerUuid the player's unique id
	 * @param questId    the quest id
	 * @return {@code true} if the quest is active and completed for the player
	 */
	boolean isCompleted(@NotNull UUID playerUuid, @NotNull String questId);

	/**
	 * Adds progress to a specific active quest for an online player. Has no effect if the
	 * quest is not active for the player.
	 *
	 * @param playerUuid the player's unique id
	 * @param questId    the quest id
	 * @param amount     the amount of progress to add (must be positive)
	 */
	void addProgress(@NotNull UUID playerUuid, @NotNull String questId, long amount);

	/**
	 * Rerolls the player's daily quest set, assigning a fresh random selection.
	 *
	 * @param playerUuid the player's unique id
	 */
	void rerollDaily(@NotNull UUID playerUuid);

	// ---------------------------------------------------------------------
	// Administration (config + offline-capable player data management)
	// ---------------------------------------------------------------------

	/**
	 * Reloads the Quests configuration from {@code quests.yml} and re-applies it to online
	 * players. Use after editing the config file externally (e.g. from the web dashboard).
	 */
	void reloadConfig();

	/**
	 * Gets all of the player's active quests across every category. Works for offline players
	 * (loaded from storage without re-rolling or persisting).
	 *
	 * @param playerUuid the player's unique id
	 * @return an immutable list of the player's active quests
	 */
	@NotNull
	List<ActiveQuest> getActiveQuests(@NotNull UUID playerUuid);

	/**
	 * Sets the player's absolute progress on a specific quest (clamped to the quest's target).
	 * Works for offline players. Has no effect if the quest is not assigned to the player.
	 *
	 * @param playerUuid the player's unique id
	 * @param questId    the quest id
	 * @param progress   the absolute progress value to set
	 */
	void setProgress(@NotNull UUID playerUuid, @NotNull String questId, long progress);

	/**
	 * Sets the claimed flag on a specific quest for the player. Works for offline players.
	 *
	 * @param playerUuid the player's unique id
	 * @param questId    the quest id
	 * @param claimed    whether the quest's reward should be marked claimed
	 */
	void setClaimed(@NotNull UUID playerUuid, @NotNull String questId, boolean claimed);

	/**
	 * Rerolls the player's quests for a category, assigning a fresh random selection. Works for
	 * offline players (their reroll is applied to storage and surfaces on next login).
	 *
	 * @param playerUuid the player's unique id
	 * @param category   the category to reroll
	 */
	void reroll(@NotNull UUID playerUuid, @NotNull QuestCategory category);

	/**
	 * Deletes all of the player's stored quest progress. Works for offline players. The player
	 * receives a fresh assignment on next login.
	 *
	 * @param playerUuid the player's unique id
	 */
	void resetPlayer(@NotNull UUID playerUuid);

	/**
	 * Deletes every player's stored quest progress (administrative bulk reset). Online players
	 * are re-assigned a fresh set immediately.
	 */
	void resetAllPlayers();
}
