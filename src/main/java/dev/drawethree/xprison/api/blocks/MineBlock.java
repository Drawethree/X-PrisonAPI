package dev.drawethree.xprison.api.blocks;

import dev.drawethree.xprison.api.blocks.impl.ItemsAdderMineBlock;
import dev.drawethree.xprison.api.blocks.impl.NexoMineBlock;
import dev.drawethree.xprison.api.blocks.impl.VanillaMineBlock;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a block that can appear inside a mine.
 * <p>
 * A {@code MineBlock} is an abstraction over different block providers,
 * allowing mines to contain both vanilla Minecraft blocks and custom blocks
 * supplied by external plugins such as ItemsAdder or Nexo.
 * <p>
 * Implementations should be immutable and uniquely identifiable by their
 * {@link #getId()} value.
 *
 * @see VanillaMineBlock
 * @see ItemsAdderMineBlock
 * @see NexoMineBlock
 */
public interface MineBlock {

	/**
	 * Checks whether this block represents a vanilla Minecraft block.
	 *
	 * @return {@code true} if this block is a vanilla block,
	 *         {@code false} if it is provided by a custom block system
	 */
	boolean isVanilla();

	/**
	 * Returns the unique identifier of this block.
	 * <p>
	 * For vanilla blocks, this is typically the material name
	 * (e.g. {@code "STONE"}, {@code "DIAMOND_ORE"}).
	 * <br>
	 * For custom blocks, this should be a namespaced identifier
	 * (e.g. {@code "myitems:ruby_ore"}).
	 *
	 * @return the unique block identifier
	 */
	String getId();

	/**
	 * Converts this MineBlock into an {@link ItemStack}.
	 * <p>
	 * This method should produce an {@link ItemStack} that accurately
	 * represents the block, including type and any custom properties if
	 * applicable (for example, ItemsAdder custom blocks).
	 * <p>
	 * The returned {@link ItemStack} should have the specified {@code amount}.
	 *
	 * @param amount the quantity of items in the stack
	 * @return an {@link ItemStack} representing this block
	 */
	ItemStack toItemStack(int amount);

	default ItemStack toItemStack() {
		return toItemStack(1);
	}
}
