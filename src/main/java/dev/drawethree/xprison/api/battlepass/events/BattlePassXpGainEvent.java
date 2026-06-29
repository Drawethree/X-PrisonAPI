package dev.drawethree.xprison.api.battlepass.events;

import dev.drawethree.xprison.api.battlepass.model.XpSource;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player is about to gain Battle Pass XP.
 * <p>
 * Cancellable, and the gained {@link #amount} may be modified by listeners (e.g. to
 * apply a multiplier) before it is applied.
 */
@Getter
public final class BattlePassXpGainEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final XpSource source;

	@Setter
	private long amount;

	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new BattlePassXpGainEvent.
	 *
	 * @param player the player gaining XP
	 * @param amount the amount of XP to be gained
	 * @param source what caused the XP gain
	 */
	public BattlePassXpGainEvent(Player player, long amount, XpSource source) {
		super(player);
		this.amount = amount;
		this.source = source;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
