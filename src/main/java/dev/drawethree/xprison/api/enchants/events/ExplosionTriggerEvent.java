package dev.drawethree.xprison.api.enchants.events;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.List;

/**
 * Event called when the Explosive enchant is triggered.
 * <p>
 * This event contains details about the player triggering the enchant,
 * the WorldGuard region where it was triggered,
 * the original block broken,
 * and the list of blocks affected by the enchant.
 * <p>
 * The event is cancellable, allowing listeners to prevent the explosion effect.
 */
@Getter
public final class ExplosionTriggerEvent extends XPrisonPlayerEnchantTriggerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	/**
	 * Whether this event has been cancelled.
	 */
	private boolean cancelled;

	/**
	 * Constructs a new {@code ExplosionTriggerEvent}.
	 *
	 * @param p              The player who triggered the enchant
	 * @param mineRegion     The WorldGuard region where the enchant was triggered
	 * @param originBlock    The original block broken that triggered the enchant
	 * @param blocksAffected The list of affected blocks marked for removal
	 */
	public ExplosionTriggerEvent(Player p, IWrappedRegion mineRegion, Block originBlock, List<Block> blocksAffected) {
		super(p, mineRegion, originBlock, blocksAffected);
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return The static handler list.
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return The static handler list.
	 */
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	/**
	 * Checks whether this event is cancelled.
	 *
	 * @return {@code true} if the event is cancelled, {@code false} otherwise.
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Sets the cancellation state of this event.
	 *
	 * @param cancel {@code true} to cancel the event, {@code false} to allow it to proceed.
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
