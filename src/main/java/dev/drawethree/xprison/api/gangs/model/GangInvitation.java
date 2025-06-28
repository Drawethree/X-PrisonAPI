package dev.drawethree.xprison.api.gangs.model;

import org.bukkit.OfflinePlayer;

import java.util.Date;

/**
 * Represents an invitation sent to a player to join a gang in the XPrison plugin.
 * Stores information about who invited whom, to which gang, and when the invitation was sent.
 */
public interface GangInvitation {

    /**
     * Gets the gang the invitation is associated with.
     *
     * @return the {@link Gang} the invited player is being invited to join
     */
    Gang getGang();

    /**
     * Gets the player who sent the invitation.
     *
     * @return the {@link OfflinePlayer} who sent the invitation
     */
    OfflinePlayer getInvitedBy();

    /**
     * Gets the player who received the invitation.
     *
     * @return the {@link OfflinePlayer} who was invited
     */
    OfflinePlayer getInvitedPlayer();

    /**
     * Gets the date and time when the invitation was created.
     *
     * @return the {@link Date} when the invitation was made
     */
    Date getInviteDate();
}
