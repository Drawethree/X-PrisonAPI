package dev.drawethree.xprison.api.gangs.model;

import org.bukkit.OfflinePlayer;

import java.util.Date;

public interface GangInvitation {

    Gang getGang();

    OfflinePlayer getInvitedBy();

    OfflinePlayer getInvitedPlayer();

    Date getInviteDate();

}