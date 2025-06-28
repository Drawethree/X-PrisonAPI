package dev.drawethree.xprison.api.gangs.model;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;


public interface Gang {

    OfflinePlayer getOwnerOffline();

    String getName();

    boolean isOwner(OfflinePlayer player);

    boolean isInGang(OfflinePlayer player);

    Collection<GangInvitation> getPendingInvites();

    long getGangValue();

    Collection<Player> getOnlineMembers();

    Collection<OfflinePlayer> getAllMembers();

    GangInvitation invitePlayer(OfflinePlayer player);

    void removeInvite(GangInvitation invitation);
}