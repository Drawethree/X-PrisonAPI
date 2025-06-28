package dev.drawethree.xprison.api.gangs.enums;

/**
 * Enum representing the reasons why a player leaves a gang.
 */
public enum GangLeaveReason {

    /**
     * Player removed from gang by console or command run by an admin.
     */
    ADMIN,

    /**
     * Player was kicked out of the gang.
     */
    KICK,

    /**
     * Player voluntarily left the gang.
     */
    LEAVE
}
