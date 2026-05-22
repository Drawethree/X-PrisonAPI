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
 * Called when a player prestiges an enchantment on their pickaxe.
 * <p>
 * Fired after all prestige requirements are validated (max level reached,
 * sufficient activations, prestige cap not exceeded) but before the prestige
 * is actually applied. Cancelling the event prevents the prestige from occurring.
 * <p>
 * The {@code newPrestige} field can be modified by a listener to change the
 * prestige tier that will be written to the item.
 */
@Getter
public final class XPrisonEnchantPrestigeEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The enchantment being prestiged.
     */
    private final XPrisonEnchantment enchantment;

    /**
     * The prestige tier the enchantment is at before this operation.
     */
    private final int oldPrestige;

    /**
     * The prestige tier the enchantment will reach after this operation.
     * Can be modified by a listener.
     */
    @Setter
    private int newPrestige;

    @Setter
    private boolean cancelled;

    /**
     * Constructs a new {@code XPrisonEnchantPrestigeEvent}.
     *
     * @param player      The player prestiging the enchantment.
     * @param enchantment The enchantment being prestiged.
     * @param oldPrestige The current prestige tier (before the operation).
     * @param newPrestige The target prestige tier (after the operation).
     */
    public XPrisonEnchantPrestigeEvent(Player player, XPrisonEnchantment enchantment,
                                       int oldPrestige, int newPrestige) {
        super(player);
        this.enchantment = enchantment;
        this.oldPrestige = oldPrestige;
        this.newPrestige = newPrestige;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
