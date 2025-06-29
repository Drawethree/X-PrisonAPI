package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a {@link XPrisonEnchantment} is registered into the system.
 * This event can be used to initialize or react to newly available enchants.
 */
@Getter
public class XPrisonEnchantRegisterEvent extends XPrisonEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The enchantment that was registered.
     */
    private final XPrisonEnchantment enchantment;

    /**
     * Constructs a new {@code XPrisonEnchantRegisterEvent}.
     *
     * @param enchantment the enchantment being registered
     */
    public XPrisonEnchantRegisterEvent(XPrisonEnchantment enchantment) {
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
