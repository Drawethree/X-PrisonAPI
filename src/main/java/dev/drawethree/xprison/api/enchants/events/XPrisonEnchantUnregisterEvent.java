package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a {@link XPrisonEnchantment} is unregistered from the system.
 * This can be used to handle cleanup or update dependent systems when an enchantment
 * becomes unavailable.
 */
@Getter
public class XPrisonEnchantUnregisterEvent extends XPrisonEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The enchantment that was unregistered.
     */
    private final XPrisonEnchantment enchantment;

    /**
     * Constructs a new {@code XPrisonEnchantUnregisterEvent}.
     *
     * @param enchantment the enchantment being unregistered
     */
    public XPrisonEnchantUnregisterEvent(XPrisonEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Returns the handler list for this event type.
     *
     * @return the static handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
