package dev.drawethree.xprison.api.pickaxeskins.events;

import dev.drawethree.xprison.api.pickaxeskins.model.PickaxeSkin;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event fired when a player is about to change the skin applied to their pickaxe
 * (applying a new skin, or removing one).
 * <p>
 * This event is {@link Cancellable}; cancelling it leaves the current skin in place.
 */
@Getter
public final class PlayerPickaxeSkinChangeEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The skin currently on the pickaxe, or {@code null} if none was applied.
	 */
	@Nullable
	private final PickaxeSkin oldSkin;

	/**
	 * The skin being applied, or {@code null} if the skin is being removed.
	 */
	@Nullable
	private final PickaxeSkin newSkin;

	/**
	 * Whether the event is cancelled.
	 */
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new PlayerPickaxeSkinChangeEvent.
	 *
	 * @param player  the player changing the skin
	 * @param oldSkin the previously-applied skin, or {@code null} if none
	 * @param newSkin the skin being applied, or {@code null} if removing
	 */
	public PlayerPickaxeSkinChangeEvent(Player player, @Nullable PickaxeSkin oldSkin, @Nullable PickaxeSkin newSkin) {
		super(player);
		this.oldSkin = oldSkin;
		this.newSkin = newSkin;
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handlers for this event instance.
	 *
	 * @return the handler list
	 */
	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
