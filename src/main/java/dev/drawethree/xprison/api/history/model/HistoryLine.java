package dev.drawethree.xprison.api.history.model;

import org.bukkit.OfflinePlayer;

import java.util.Date;

public interface HistoryLine {

    OfflinePlayer getOfflinePlayer();

    String getModule();

    String getContext();

    Date getCreatedAt();
}