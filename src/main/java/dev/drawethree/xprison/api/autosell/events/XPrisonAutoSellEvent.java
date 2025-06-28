package dev.drawethree.xprison.api.autosell.events;

import dev.drawethree.xprison.api.autosell.model.AutoSellItemStack;
import dev.drawethree.xprison.api.autosell.model.SellRegion;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.Map;

/**
 * Event called when mined blocks are automatically sold.
 */
@Getter
public final class XPrisonAutoSellEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final SellRegion region;

    @Setter
    private Map<AutoSellItemStack, Double> itemsToSell;

    @Setter
    private boolean cancelled;

    /**
     * Constructs a new auto-sell event.
     *
     * @param player      The player who mined the blocks and triggered auto-sell
     * @param reg         The sell region where the blocks were mined
     * @param itemsToSell A map of items to be sold with their respective prices
     */
    public XPrisonAutoSellEvent(Player player, SellRegion reg, Map<AutoSellItemStack, Double> itemsToSell) {
        super(player);
        this.player = player;
        this.region = reg;
        this.itemsToSell = itemsToSell;
    }

    /**
     * Returns the list of registered event handlers.
     *
     * @return HandlerList for this event
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the list of registered event handlers.
     *
     * @return HandlerList for this event
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
