package dev.drawethree.xprison.api.mines.model;

import com.cryptomorin.xseries.XMaterial;

/**
 * Represents a palette of blocks with associated percentages,
 * used for defining the composition of blocks in a mine.
 */
public interface BlockPalette {

    /**
     * Checks if the palette contains the specified material.
     *
     * @param material the {@link XMaterial} to check
     * @return true if the material is present, false otherwise
     */
    boolean contains(XMaterial material);

    /**
     * Gets the percentage of the specified material in the palette.
     *
     * @param material the {@link XMaterial} to get the percentage for
     * @return the percentage (0.0 to 1.0) of the material in the palette
     */
    double getPercentage(XMaterial material);

    /**
     * Sets the percentage for the specified material in the palette.
     *
     * @param material   the {@link XMaterial} to set the percentage for
     * @param percentage the new percentage value (0.0 to 1.0)
     */
    void setPercentage(XMaterial material, double percentage);

    /**
     * Adds a material to the palette with a given percentage.
     *
     * @param material   the {@link XMaterial} to add
     * @param percentage the percentage (0.0 to 1.0) of the material in the palette
     */
    void add(XMaterial material, double percentage);

    /**
     * Removes the specified material from the palette.
     *
     * @param material the {@link XMaterial} to remove
     */
    void remove(XMaterial material);
}
