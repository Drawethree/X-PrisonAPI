package dev.drawethree.xprison.api.multipliers.model;

import org.bukkit.OfflinePlayer;

public interface PlayerMultiplier extends Multiplier {

    OfflinePlayer getOfflinePlayer();
}