package dev.drawethree.xprison.api.quests.events;

import dev.drawethree.xprison.api.quests.model.Quest;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player claims the reward for a completed quest.
 */
@Getter
public final class QuestClaimEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Quest quest;

	public QuestClaimEvent(Player player, Quest quest) {
		super(player);
		this.quest = quest;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
