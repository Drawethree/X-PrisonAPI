package dev.drawethree.xprison.api.pickaxequality.event;

import dev.drawethree.xprison.api.pickaxequality.model.PickaxeQualityTier;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Event fired before a pickaxe's quality tier is upgraded, after the player has been found eligible
 * but before they are charged.
 * <p>
 * Listeners may change the {@link #getCost() cost} - for example to grant a discount - or cancel the
 * event to block the upgrade entirely. A cancelled event charges nothing and leaves the pickaxe
 * untouched.
 */
@Getter
public final class PlayerPickaxeQualityUpgradeEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Setter
    private boolean cancelled;

    /**
     * The player upgrading their pickaxe.
     */
    private final Player player;

    /**
     * The pickaxe being upgraded, in its state before the upgrade is applied.
     */
    private final ItemStack pickaxe;

    /**
     * The tier the pickaxe currently has.
     */
    private final PickaxeQualityTier from;

    /**
     * The tier the pickaxe is being upgraded to.
     */
    private final PickaxeQualityTier to;

    /**
     * The exact amount the player is about to be charged, in the {@link #getTo() target tier}'s
     * cost currency. Can be modified by listeners; values below zero are treated as zero.
     */
    @Setter
    private BigDecimal cost;

    /**
     * Constructs a new PlayerPickaxeQualityUpgradeEvent.
     *
     * @param player  the player upgrading their pickaxe
     * @param pickaxe the pickaxe being upgraded (pre-upgrade state)
     * @param from    the pickaxe's current tier
     * @param to      the tier being bought
     * @param cost    the amount the player is about to be charged
     */
    public PlayerPickaxeQualityUpgradeEvent(Player player, ItemStack pickaxe, PickaxeQualityTier from, PickaxeQualityTier to, BigDecimal cost) {
        super(player);
        this.player = player;
        this.pickaxe = pickaxe;
        this.from = from;
        this.to = to;
        this.cost = cost;
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
