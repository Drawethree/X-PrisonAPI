package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called **before** a {@link XPrisonEnchantment} attempts to trigger based on its chance.
 * <p>
 * This event allows you to:
 * <ul>
 *     <li>Cancel the enchant from triggering entirely.</li>
 *     <li>Modify the actual chance to trigger before the roll happens.</li>
 * </ul>
 *
 * Useful for enchantments like boosters or modifiers that affect other enchants’ proc rates.
 */
public class XPrisonEnchantPreTriggerEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * -- GETTER --
     *  Gets the enchantment that is attempting to trigger.
     *
     * @return The enchantment being processed.
     */
    @Getter
    private final XPrisonEnchantment enchantment;
    /**
     * -- GETTER --
     *  Gets the level of the enchantment.
     *
     * @return The enchantment level.
     */
    @Getter
    private final int level;
    /**
     * -- GETTER --
     *  Gets the current chance for the enchantment to trigger.
     *
     *
     * -- SETTER --
     *  Sets a new chance for the enchantment to trigger.
     *
     @return The chance to trigger (between 0.0 and 1.0).
      * @param chanceToTrigger The new chance value (between 0.0 and 1.0).
     */
    @Setter
    @Getter
    private double chanceToTrigger;
    private boolean cancelled;

    /**
     * Constructs a new {@code XPrisonEnchantPreTriggerEvent}.
     *
     * @param player           The player using the enchantment.
     * @param enchantment      The enchantment being triggered.
     * @param level            The level of the enchantment.
     * @param chanceToTrigger  The current chance to trigger (before random roll).
     */
    public XPrisonEnchantPreTriggerEvent(Player player, XPrisonEnchantment enchantment, int level, double chanceToTrigger) {
        super(player);
        this.enchantment = enchantment;
        this.level = level;
        this.chanceToTrigger = chanceToTrigger;
    }

    /**
     * Checks if the event has been cancelled.
     *
     * @return true if the enchant should not trigger; false otherwise.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether or not the enchantment should be cancelled from triggering.
     *
     * @param cancelled true to prevent the enchantment from triggering.
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the handlers for this event.
     *
     * @return The handler list.
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Bukkit-required method to retrieve handler list.
     *
     * @return The shared handler list.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
