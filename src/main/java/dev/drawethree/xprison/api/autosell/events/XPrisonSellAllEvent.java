package dev.drawethree.xprison.api.autosell.events;

import dev.drawethree.xprison.api.autosell.model.AutoSellItemStack;
import dev.drawethree.xprison.api.autosell.model.SellRegion;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Event called when a player uses the /sellall command to sell multiple items at once.
 */
@Getter
public final class XPrisonSellAllEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;

	@Getter
	@Setter
	private Map<AutoSellItemStack, Double> itemsToSell;

    @Nullable
    private final SellRegion region;

    @Getter
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new sell-all event.
	 *
	 * @param player      The player performing the sell-all
	 * @param itemsToSell A map of items to sell and their respective prices
     * @param region         The sell region where the blocks were mined, can be null
	 */
	public XPrisonSellAllEvent(Player player, Map<AutoSellItemStack, Double> itemsToSell, @Nullable SellRegion region) {
		super(player);
		this.player = player;
		this.itemsToSell = itemsToSell;
        this.region = region;
    }

	/**
	 * Gets the list of event handlers.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
