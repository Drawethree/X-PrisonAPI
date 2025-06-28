package dev.drawethree.xprison.api.gangs.model;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Represents a Gang in the XPrison plugin.
 * Provides methods to access gang data, members, invitations, and gang-related actions.
 */
public interface Gang {

    /**
     * Gets the gang leader as an {@link OfflinePlayer}.
     *
     * @return the gang leader
     */
    OfflinePlayer getOwnerOffline();

    /**
     * Gets the name of the gang.
     *
     * @return the gang name
     */
    String getName();

    /**
     * Checks whether the given player is the leader of this gang.
     *
     * @param player the player to check
     * @return {@code true} if the player is the gang leader, {@code false} otherwise
     */
    boolean isOwner(OfflinePlayer player);

    /**
     * Checks whether the given player is a member of this gang.
     *
     * @param player the player to check
     * @return {@code true} if the player is in the gang, {@code false} otherwise
     */
    boolean isInGang(OfflinePlayer player);

    /**
     * Retrieves a collection of all pending invitations to this gang.
     *
     * @return a collection of pending {@link GangInvitation}s
     */
    Collection<GangInvitation> getPendingInvites();

    /**
     * Gets the current value of the gang.
     * This could represent a score, worth, or ranking metric.
     *
     * @return the numerical value of the gang
     */
    long getGangValue();

    /**
     * Retrieves a collection of all online members currently in the gang.
     *
     * @return a collection of online {@link Player}s in the gang
     */
    Collection<Player> getOnlineMembers();

    /**
     * Retrieves all members of the gang, including offline players.
     *
     * @return a collection of all {@link OfflinePlayer}s in the gang
     */
    Collection<OfflinePlayer> getAllMembers();

    /**
     * Invites a player to join this gang.
     * The invitation must be accepted before the player becomes a member.
     *
     * @param player the player to invite
     * @return the created {@link GangInvitation}
     */
    GangInvitation invitePlayer(OfflinePlayer player);

    /**
     * Removes a pending invitation from this gang.
     * If the invitation has already been accepted or expired, this may have no effect.
     *
     * @param invitation the {@link GangInvitation} to remove
     */
    void removeInvite(GangInvitation invitation);
}
