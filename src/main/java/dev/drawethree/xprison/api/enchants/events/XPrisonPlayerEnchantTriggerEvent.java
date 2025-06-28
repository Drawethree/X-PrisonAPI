package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.List;

/**
 * Abstract event class for triggering chance-based enchantments related to a player.
 * This event holds information about the player, the mine region where the event was triggered,
 * the originating block, and the list of blocks affected by the enchantment.
 */
@Getter
public abstract class XPrisonPlayerEnchantTriggerEvent extends XPrisonPlayerEvent implements Cancellable {

	protected final Player player;
	protected final IWrappedRegion mineRegion;
	protected final Block originBlock;
	protected final List<Block> blocksAffected;

	private boolean cancelled;

	/**
	 * Constructs a new enchant trigger event.
	 *
	 * @param player         Player who triggered the enchant
	 * @param mineRegion     Region where the enchant was triggered
	 * @param originBlock    Block that triggered the enchantment
	 * @param blocksAffected Blocks that will be affected by the enchantment
	 */
	public XPrisonPlayerEnchantTriggerEvent(Player player, IWrappedRegion mineRegion, Block originBlock, List<Block> blocksAffected) {
		super(player);
		this.player = player;
		this.mineRegion = mineRegion;
		this.originBlock = originBlock;
		this.blocksAffected = blocksAffected;
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
