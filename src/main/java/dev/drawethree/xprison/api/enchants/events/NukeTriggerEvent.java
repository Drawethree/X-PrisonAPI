package dev.drawethree.xprison.api.enchants.events;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.List;

/**
 * Event called when the Nuke enchantment is triggered by a player.
 * <p>
 * This event provides information about the player who triggered the enchant,
 * the WorldGuard region in which it occurred, the original block that caused the trigger,
 * and the list of blocks affected by the nuke effect.
 * <p>
 * The event is cancellable. If cancelled, the nuke effect will not proceed.
 */
@Getter
public final class NukeTriggerEvent extends XPrisonPlayerEnchantTriggerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * Constructs a new NukeTriggerEvent.
	 *
	 * @param player      The player who triggered the nuke enchantment.
	 * @param mineRegion  The WorldGuard region where the enchantment was triggered.
	 * @param originBlock The original block broken that triggered the enchant.
	 * @param blocks      The list of blocks that will be affected by the nuke (marked for removal).
	 */
	public NukeTriggerEvent(Player player, IWrappedRegion mineRegion, Block originBlock, List<Block> blocks) {
		super(player, mineRegion, originBlock, blocks);
	}

	/**
	 * Gets the handler list for this event.
	 *
	 * @return The static HandlerList instance.
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	/**
	 * Gets the handler list for this event instance.
	 *
	 * @return The static HandlerList instance.
	 */
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	/**
	 * Checks if this event is cancelled.
	 *
	 * @return True if the event is cancelled, false otherwise.
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Sets the cancellation state of this event.
	 *
	 * @param cancel True to cancel the event, false to proceed.
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
