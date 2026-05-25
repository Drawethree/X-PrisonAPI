package dev.drawethree.xprison.api.mines.model;

import dev.drawethree.xprison.api.blocks.MineBlock;

import java.util.Map;
import java.util.Set;

/**
 * Represents a weighted palette of blocks used to define the composition
 * of a mine.
 * <p>
 * A {@code BlockPalette} can contain both vanilla Minecraft blocks and
 * custom blocks provided by external plugins (e.g. ItemsAdder), via the
 * {@link MineBlock} abstraction.
 * <p>
 * Percentages are expressed as values between {@code 0.0} and {@code 1.0},
 * where {@code 1.0} represents 100% of the mine composition.
 */
public interface BlockPalette {

	/**
	 * Checks whether this palette contains the specified block.
	 *
	 * @param mineBlock the {@link MineBlock} to check
	 * @return {@code true} if the block is present in the palette,
	 *         {@code false} otherwise
	 */
	boolean contains(MineBlock mineBlock);

	/**
	 * Gets the percentage of the specified block in this palette.
	 *
	 * @param mineBlock the {@link MineBlock} to get the percentage for
	 * @return the percentage ({@code 0.0} to {@code 1.0}) of the block
	 */
	double getPercentage(MineBlock mineBlock);

	/**
	 * Sets the percentage of the specified block in this palette.
	 * <p>
	 * Implementations may choose to normalize the palette or enforce
	 * a maximum total percentage of {@code 1.0}.
	 *
	 * @param mineBlock  the {@link MineBlock} to set the percentage for
	 * @param percentage the new percentage value ({@code 0.0} to {@code 1.0})
	 */
	void setPercentage(MineBlock mineBlock, double percentage);

	/**
	 * Adds a block to this palette with the given percentage.
	 * <p>
	 * If the block already exists in the palette, implementations may
	 * either overwrite the existing value or throw an exception.
	 *
	 * @param mineBlock  the {@link MineBlock} to add
	 * @param percentage the percentage ({@code 0.0} to {@code 1.0}) of the block
	 */
	void add(MineBlock mineBlock, double percentage);

	/**
	 * Removes the specified block from this palette.
	 *
	 * @param mineBlock the {@link MineBlock} to remove
	 */
	void remove(MineBlock mineBlock);

	/**
	 * Returns all blocks in this palette (including those with 0% percentage).
	 *
	 * @return the set of all {@link MineBlock} entries in the palette
	 */
	Set<MineBlock> getBlocks();

	/**
	 * Replaces the entire palette with the given map of block IDs to percentages.
	 * Block IDs use the vanilla material name (e.g. {@code "DIAMOND_ORE"}) or a
	 * namespaced custom block id (e.g. {@code "myitems:ruby_ore"}).
	 * Percentages are in the range {@code 0.0}–{@code 1.0}.
	 * Implementations should ignore unknown IDs rather than throwing.
	 *
	 * @param entries map of block ID → percentage
	 */
	void setPaletteByIds(Map<String, Double> entries);
}
