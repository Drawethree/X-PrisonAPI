package dev.drawethree.xprison.api.rebirth.model;

import java.util.List;

public interface Rebirth {

    /**
     * @return The numeric ID of the rebirth (1, 2, 3, ...)
     */
    int getId();

    /**
     * @return The chat prefix for this rebirth.
     */
    String getPrefix();

    /**
     * @return All requirements needed to achieve this rebirth.
     */
    List<RebirthRequirement> getRequirements();

    /**
     * @return Commands to execute when the player reaches this rebirth.
     */
    List<String> getRewardCommands();
}
