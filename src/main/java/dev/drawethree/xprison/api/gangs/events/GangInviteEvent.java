package dev.drawethree.xprison.api.gangs.events;

import dev.drawethree.xprison.api.gangs.model.Gang;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player is invited to a gang.
 * <p>
 * The {@link #getPlayer()} is the invited (target) player.
 * This event is {@link Cancellable}, allowing plugins to prevent the invitation.
 */
public final class GangInviteEvent extends XPrisonPlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean cancelled;

	/**
	 * The player who sent the invitation.
	 */
	@Getter
	private final OfflinePlayer inviter;

	/**
	 * The gang the player is being invited to.
	 */
	@Getter
	private final Gang gang;

	/**
	 * Constructs a new {@link GangInviteEvent}.
	 *
	 * @param inviter the {@link OfflinePlayer} who sent the invitation
	 * @param invited the {@link OfflinePlayer} being invited (also {@link #getPlayer()})
	 * @param gang    the {@link Gang} the player is invited to
	 */
	public GangInviteEvent(OfflinePlayer inviter, OfflinePlayer invited, Gang gang) {
		super(invited);
		this.inviter = inviter;
		this.gang = gang;
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
