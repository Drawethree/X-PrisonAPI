package dev.drawethree.xprison.api.gems.events;

import dev.drawethree.xprison.api.shared.currency.enums.LostCause;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event that is called when a player loses gems.
 * <p>
 * This event can be used to track or log gem deductions from players due to specific causes.
 * The amount lost can be modified using {@link #setAmount(long)}.
 */
@Getter
public final class PlayerGemsLostEvent extends XPrisonPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The cause for which the player is losing gems.
	 */
	private final LostCause cause;

	/**
	 * The amount of gems the player is losing.
	 * Can be modified using {@link #setAmount(long)}.
	 */
	@Setter
	private long amount;

	/**
	 * Constructs a new {@link PlayerGemsLostEvent}.
	 *
	 * @param cause  the {@link LostCause} indicating why the gems are being lost
	 * @param player the {@link OfflinePlayer} who is losing gems
	 * @param amount the amount of gems being lost
	 */
	public PlayerGemsLostEvent(LostCause cause, OfflinePlayer player, long amount) {
		super(player);
		this.cause = cause;
		this.amount = amount;
	}

	/**
	 * Returns the static handler list for this event class.
	 *
	 * @return the {@link HandlerList}
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Returns the handlers for this specific event instance.
	 *
	 * @return the {@link HandlerList}
	 */
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
