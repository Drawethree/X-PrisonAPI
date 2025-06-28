package dev.drawethree.xprison.api.mines.model;

import me.lucko.helper.serialize.Point;
import me.lucko.helper.serialize.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Date;

public interface Mine {

    String getName();

    Region getRegion();

    Point getTeleportLocation();

    BlockPalette getBlockPalette();

    double getResetPercentage();

    int getTotalBlocks();

    int getCurrentBlocks();

    boolean isResetting();

    Date getNextResetDate();

    boolean isInMine(Location location);

    Collection<Player> getPlayersInMine();

    void addEffect(PotionEffectType potionEffectType, int level);

    void removeEffect(PotionEffectType potionEffectType);

}