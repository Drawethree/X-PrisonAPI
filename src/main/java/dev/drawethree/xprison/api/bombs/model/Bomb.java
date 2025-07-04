package dev.drawethree.xprison.api.bombs.model;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a Bomb in the XPrison plugin.
 * A Bomb has various properties such as name, radius, visual representation, sounds, and explosion delay.
 */
public interface Bomb {

    /**
     * Gets the unique name of the bomb.
     *
     * @return the name of the bomb
     */
    String getName();

    /**
     * Gets the radius of the bomb's explosion effect.
     *
     * @return the radius of the explosion
     */
    int getRadius();

    /**
     * Gets the visual item representation of the bomb.
     *
     * @return the {@link ItemStack} that represents this bomb
     */
    ItemStack getItem();

    /**
     * Gets the sound played when the bomb is dropped.
     *
     * @return the {@link Sound} to be played on bomb drop
     */
    Sound getDropSound();

    /**
     * Gets the sound played when the bomb explodes.
     *
     * @return the {@link Sound} to be played on explosion
     */
    Sound getExplodeSound();

    /**
     * Gets the delay before the bomb explodes after being placed or dropped, in ticks.
     *
     * @return the explosion delay in server ticks (1 tick = 1/20th of a second)
     */
    int getExplosionDelay();
}
