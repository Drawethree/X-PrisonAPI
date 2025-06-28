package dev.drawethree.xprison.api.mines.model;


import me.lucko.helper.serialize.Position;

public interface MineSelection {

    Position getPosition1();

    Position getPosition2();

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