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
 * Event called when a player uses the /sellall command to sell multiple items at once.
 */
@Getter
public final class XPrisonSellAllEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final SellRegion region;

	@Getter
	@Setter
	private Map<AutoSellItemStack, Double> itemsToSell;

	@Getter
	@Setter
	private boolean cancelled;

	/**
	 * Constructs a new sell-all event.
	 *
	 * @param player      The player performing the sell-all
	 * @param reg         The sell region where the blocks were mined
	 * @param itemsToSell A map of items to sell and their respective prices
	 */
	public XPrisonSellAllEvent(Player player, SellRegion reg, Map<AutoSellItemStack, Double> itemsToSell) {
		super(player);
		this.player = player;
		this.region = reg;
		this.itemsToSell = itemsToSell;
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
