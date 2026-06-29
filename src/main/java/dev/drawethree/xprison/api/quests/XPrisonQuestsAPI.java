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
}
