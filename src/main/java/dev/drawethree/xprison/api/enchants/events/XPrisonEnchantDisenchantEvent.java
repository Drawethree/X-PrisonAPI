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
 * Called when a player removes levels from an enchantment via the disenchant / refund flow.
 * <p>
 * The event is fired <b>before</b> the levels are removed and any currency is refunded, and is
 * cancellable so listeners can veto a disenchant. Listeners may also adjust {@link #getRefundAmount()
 * the refund amount} — for admin disenchants (which pay nothing) this is {@code 0}.
 */
@Getter
public final class XPrisonEnchantDisenchantEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final XPrisonEnchantment enchantment;
    /** The enchant level on the pickaxe before this disenchant. */
    private final int currentLevel;
    /** How many levels are being removed. */
    private final int levelsRemoved;
    /** Whether this is an admin disenchant (no currency refunded). */
    private final boolean admin;
    /** Currency that will be refunded to the player. Listeners may change this. */
    @Setter
    private long refundAmount;

    private boolean cancelled;

    public XPrisonEnchantDisenchantEvent(Player player, XPrisonEnchantment enchantment, int currentLevel,
                                         int levelsRemoved, long refundAmount, boolean admin) {
        super(player);
        this.enchantment = enchantment;
        this.currentLevel = currentLevel;
        this.levelsRemoved = levelsRemoved;
        this.refundAmount = refundAmount;
        this.admin = admin;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
