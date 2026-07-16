package dev.drawethree.xprison.api.pickaxelevels.event;

import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeExpSource;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired whenever a pickaxe is about to gain experience.
 * <p>
 * The {@link #getAmount() amount} is the final value after per-block experience values and the
 * configured source multiplier have been applied. Listeners may modify it via
 * {@link #setAmount(long)} (values {@code <= 0} suppress the gain) or cancel the event entirely
 * to prevent the experience from being awarded.
 */
@Getter
public final class PlayerPickaxeExpGainEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Setter
    private boolean cancelled;

    /**
     * The player whose pickaxe is gaining experience.
     */
    private final Player player;

    /**
     * The pickaxe being progressed, in its state before this gain is applied.
     */
    private final ItemStack pickaxe;

    /**
     * The source of this experience gain.
     */
    private final PickaxeExpSource source;

    /**
     * The amount of experience to award, after per-block values and source multipliers.
     * Can be modified by listeners; values {@code <= 0} suppress the gain.
     */
    @Setter
    private long amount;

    /**
     * Constructs a new PlayerPickaxeExpGainEvent.
     *
     * @param player  the player whose pickaxe is gaining experience
     * @param pickaxe the pickaxe being progressed (pre-gain state)
     * @param source  the source of the gain
     * @param amount  the amount of experience to award
     */
    public PlayerPickaxeExpGainEvent(Player player, ItemStack pickaxe, PickaxeExpSource source, long amount) {
        super(player);
        this.player = player;
        this.pickaxe = pickaxe;
        this.source = source;
        this.amount = amount;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Gets the handlers for this event.
     *
     * @return the handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
