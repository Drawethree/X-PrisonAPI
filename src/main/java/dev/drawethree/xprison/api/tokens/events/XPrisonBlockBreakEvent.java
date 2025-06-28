package dev.drawethree.xprison.api.tokens.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Event fired when a player breaks blocks within a mine, potentially with custom enchantments.
 */
@Getter
@Setter
public final class XPrisonBlockBreakEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	/**
	 * The player who broke the blocks.
	 */
	private Player player;

	/**
	 * The list of blocks that were broken.
	 */
	private List<Block> blocks;

	/**
	 * Whether the event is cancelled.
	 */
	private boolean cancelled;

	/**
	 * Constructs a new XPrisonBlockBreakEvent.
	 *
	 * @param player the player who broke the blocks
	 * @param blocks the list of blocks that were broken
	 */
	public XPrisonBlockBreakEvent(Player player, List<Block> blocks) {
		super(player);
		this.player = player;
		this.blocks = blocks;
	}

	/**
	 * Returns the static handler list for this event.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Returns the handler list for this event instance.
	 *
	 * @return the handler list
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
