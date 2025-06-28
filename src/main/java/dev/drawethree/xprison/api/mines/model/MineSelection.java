package dev.drawethree.xprison.api.mines.model;

import me.lucko.helper.serialize.Position;

/**
 * Represents a selection of two positions defining a mine's boundaries.
 */
public interface MineSelection {

    /**
     * Gets the first position of the mine selection.
     *
     * @return The first {@link Position}
     */
    Position getPosition1();

    /**
     * Gets the second position of the mine selection.
     *
     * @return The second {@link Position}
     */
    Position getPosition2();

    /**
     * Creates a new {@link MineSelection} instance with the given two positions.
     *
     * @param position1 The first position of the selection
     * @param position2 The second position of the selection
     * @return A new {@link MineSelection} instance representing the two positions
     */
    static MineSelection of(Position position1, Position position2) {
        return new MineSelection() {

            @Override
            public Position getPosition1() {
                return position1;
            }

            @Override
            public Position getPosition2() {
                return position2;
            }
        };
    }
}
