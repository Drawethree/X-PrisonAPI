package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a gang's value changes (admin modify, set or add).
 * <p>
 * This event is {@link Cancellable}, allowing plugins to prevent the value change.
 * The resulting {@code newValue} may be adjusted by listeners via {@link #setNewValue(long)}.
 */
public final class GangValueChangeEvent extends XPrisonEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The gang whose value is changing.
	 */
	@Getter
	private final Gang gang;

	/**
	 * The gang's value before the change.
	 */
	@Getter
	private final long oldValue;

	/**
	 * The gang's value after the change. Mutable so listeners can adjust the final value.
	 */
	@Getter
	@Setter
	private long newValue;

	/**
	 * Constructs a new {@link GangValueChangeEvent}.
	 *
	 * @param gang     the {@link Gang} whose value is changing
	 * @param oldValue the value before the change
	 * @param newValue the value after the change
	 */
	public GangValueChangeEvent(Gang gang, long oldValue, long newValue) {
		this.gang = gang;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Gets the static handler list for this event.
	 *
	 * @return the {@link HandlerList}
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
