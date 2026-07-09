package dev.drawethree.xprison.api.multipliers.events;

import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player's personal multiplier is reset for a currency.
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the reset.
 */
@Getter
public final class PlayerMultiplierResetEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private final XPrisonCurrency currency;

	private final double previousMultiplier;

	/**
	 * Constructs a new {@link PlayerMultiplierResetEvent}.
	 *
	 * @param player             the player whose multiplier is reset
	 * @param currency           the currency affected
	 * @param previousMultiplier the multiplier value before the reset
	 */
	public PlayerMultiplierResetEvent(Player player, XPrisonCurrency currency, double previousMultiplier) {
		super(player);
		this.currency = currency;
		this.previousMultiplier = previousMultiplier;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
