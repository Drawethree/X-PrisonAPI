package dev.drawethree.xprison.api.prestiges.events;

import dev.drawethree.xprison.api.prestiges.model.Prestige;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class PlayerPrestigeEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Prestige oldPrestige;

	@Setter
	private Prestige newPrestige;

	@Setter
	private boolean cancelled;

	/**
	 * Called when player changes prestige
	 *
	 * @param player      Player
	 * @param oldPrestige old prestige
	 * @param newPrestige new prestige
	 */
	public PlayerPrestigeEvent(Player player, Prestige oldPrestige, Prestige newPrestige) {
		super(player);
		this.oldPrestige = oldPrestige;
		this.newPrestige = newPrestige;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
