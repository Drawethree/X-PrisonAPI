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
	 * Creates a MineBlock from a custom block ID owned by one of the registered custom-content
	 * providers. Examples: ItemsAdder {@code "myitems:ruby_ore"} or Nexo {@code "nexo:sapphire_ore"}.
	 *
	 * @param id the custom block ID
	 * @return a new MineBlock instance, or {@code null} if no provider owns the id
	 */
	MineBlock fromCustom(String id);

	/**
	 * Creates a MineBlock from an {@link ItemStack}.
	 * Will automatically detect if it's vanilla or custom (e.g. ItemsAdder / Nexo).
	 *
	 * @param itemStack the item stack
	 * @return a new MineBlock instance
	 */
	MineBlock fromItemStack(ItemStack itemStack);

	MineBlock fromBlock(org.bukkit.block.Block block);

	/**
	 * Creates a MineBlock from a string block ID.
	 * If the id contains {@code ':'} it is treated as a custom (ItemsAdder) block;
	 * otherwise it is matched as a vanilla material name (case-insensitive).
	 *
	 * @param id the block ID, e.g. {@code "STONE"} or {@code "namespace:id"}
	 * @return a new MineBlock, or {@code null} if the id cannot be resolved
	 */
	MineBlock fromId(String id);
}
