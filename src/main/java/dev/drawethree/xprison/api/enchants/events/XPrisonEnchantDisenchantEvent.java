package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import dev.drawethree.xprison.api.utils.BigNumbers;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

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
    /**
     * Currency that will be refunded to the player, clamped to {@code long} range. Kept in sync with
     * {@link #refundAmountExact}; prefer {@link #getRefundAmountExact()}.
     */
    private long refundAmount;
    /** Exact currency that will be refunded to the player. Listeners may change this. */
    private BigDecimal refundAmountExact;

    private boolean cancelled;

    public XPrisonEnchantDisenchantEvent(Player player, XPrisonEnchantment enchantment, int currentLevel,
                                         int levelsRemoved, BigDecimal refundAmount, boolean admin) {
        super(player);
        this.enchantment = enchantment;
        this.currentLevel = currentLevel;
        this.levelsRemoved = levelsRemoved;
        this.refundAmountExact = refundAmount;
        this.refundAmount = BigNumbers.clampToLong(refundAmount);
        this.admin = admin;
    }

    /**
     * @deprecated a {@code long} refund saturates for high-level curves;
     *             use {@link #XPrisonEnchantDisenchantEvent(Player, XPrisonEnchantment, int, int, BigDecimal, boolean)}.
     */
    @Deprecated
    public XPrisonEnchantDisenchantEvent(Player player, XPrisonEnchantment enchantment, int currentLevel,
                                         int levelsRemoved, long refundAmount, boolean admin) {
        this(player, enchantment, currentLevel, levelsRemoved, BigDecimal.valueOf(refundAmount), admin);
    }

    /**
     * Sets the exact refund amount, keeping the saturating {@code long} view in sync.
     *
     * @param refundAmount the exact currency to refund
     */
    public void setRefundAmountExact(BigDecimal refundAmount) {
        this.refundAmountExact = refundAmount;
        this.refundAmount = BigNumbers.clampToLong(refundAmount);
    }

    /**
     * @param refundAmount the currency to refund
     * @deprecated a {@code long} refund saturates for high-level curves; use {@link #setRefundAmountExact(BigDecimal)}.
     */
    @Deprecated
    public void setRefundAmount(long refundAmount) {
        setRefundAmountExact(BigDecimal.valueOf(refundAmount));
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
