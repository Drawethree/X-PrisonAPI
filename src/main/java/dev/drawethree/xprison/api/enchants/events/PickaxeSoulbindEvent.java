package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a pickaxe is soulbound to a player (including automatic bind-on-first-hold).
 * <p>
 * The {@link #getPlayer()} is the new owner the pickaxe is being bound to.
 * This event is {@link Cancellable}, allowing plugins to prevent the soulbind.
 */
@Getter
public final class PickaxeSoulbindEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	/**
	 * The pickaxe being soulbound.
	 */
	private final ItemStack itemStack;

	/**
	 * Constructs a new {@link PickaxeSoulbindEvent}.
	 *
	 * @param owner     the new owner the pickaxe is being bound to
	 * @param itemStack the pickaxe being soulbound
	 */
	public PickaxeSoulbindEvent(Player owner, ItemStack itemStack) {
		super(owner);
		this.itemStack = itemStack;
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
