package dev.drawethree.xprison.api.blocks.factory;

import com.cryptomorin.xseries.XMaterial;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.inventory.ItemStack;

/**
 * Factory interface for creating {@link MineBlock} instances.
 * Supports both vanilla and custom blocks.
 */
public interface MineBlockFactory {

	/**
	 * Creates a MineBlock from a vanilla {@link XMaterial}.
	 *
	 * @param material the vanilla material
	 * @return a new MineBlock instance
	 */
	MineBlock fromVanilla(XMaterial material);

	/**
	 * Creates a MineBlock from a namespaced custom block ID.
	 * Example: ItemsAdder block ID "myitems:ruby_ore".
	 *
	 * @param id the namespaced ID of the custom block
	 * @return a new MineBlock instance
	 */
	MineBlock fromCustom(String id);

	/**
	 * Creates a MineBlock from an {@link ItemStack}.
	 * Will automatically detect if it's vanilla or custom (e.g., ItemsAdder).
	 *
	 * @param itemStack the item stack
	 * @return a new MineBlock instance
	 */
	MineBlock fromItemStack(ItemStack itemStack);

	MineBlock fromBlock(org.bukkit.block.Block block);
}
