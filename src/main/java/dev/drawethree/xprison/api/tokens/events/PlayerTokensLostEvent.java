package dev.drawethree.xprison.api.tokens.events;

import dev.drawethree.xprison.api.shared.currency.enums.LostCause;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player loses tokens.
 */
@Getter
public final class PlayerTokensLostEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The cause/reason why the player lost tokens.
	 */
	private final LostCause cause;

	/**
	 * The amount of tokens lost by the player.
	 */
	@Setter
	private long amount;

	/**
	 * Constructs a new PlayerTokensLostEvent.
	 *
	 * @param cause  the cause of token loss
	 * @param player the player who lost tokens
	 * @param amount the amount of tokens lost
	 */
	public PlayerTokensLostEvent(LostCause cause, OfflinePlayer player, long amount) {
		super(player);
		this.cause = cause;
		this.amount = amount;
	}

	/**
	 * Gets the HandlerList for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
