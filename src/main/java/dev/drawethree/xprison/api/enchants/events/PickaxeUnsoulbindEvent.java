package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event fired when a pickaxe's soulbind is cleared (e.g. via {@code /unsoulbind}).
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the soulbind from being removed.
 */
@Getter
public final class PickaxeUnsoulbindEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	/**
	 * The player the pickaxe was previously soulbound to, or {@code null} if it was unbound.
	 */
	@Nullable
	private final OfflinePlayer previousOwner;

	/**
	 * The pickaxe whose soulbind is being cleared.
	 */
	private final ItemStack itemStack;

	/**
	 * Constructs a new {@link PickaxeUnsoulbindEvent}.
	 *
	 * @param previousOwner the previous owner, or {@code null} if the pickaxe was unbound
	 * @param itemStack     the pickaxe whose soulbind is being cleared
	 */
	public PickaxeUnsoulbindEvent(@Nullable OfflinePlayer previousOwner, ItemStack itemStack) {
		this.previousOwner = previousOwner;
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
