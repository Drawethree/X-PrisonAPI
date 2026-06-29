package dev.drawethree.xprison.api.quests.events;

import dev.drawethree.xprison.api.quests.model.QuestCategory;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Fired when a fresh set of quests is assigned to a player for a rotating category
 * (e.g. on daily/weekly rotation or a manual reroll).
 */
@Getter
public final class QuestAssignEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final QuestCategory category;

	public QuestAssignEvent(Player player, QuestCategory category) {
		super(player);
		this.category = category;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
