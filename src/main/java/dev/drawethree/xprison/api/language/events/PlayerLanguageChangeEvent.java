package dev.drawethree.xprison.api.language.events;

import dev.drawethree.xprison.api.shared.events.XPrisonEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired after a player's language choice changes, through {@code /xprison language} or
 * {@link dev.drawethree.xprison.api.language.XPrisonLanguageAPI#setLanguage(UUID, String)}.
 *
 * <p>This is an informational event and is not cancellable. It always fires on the main thread.
 * It does <em>not</em> fire when the server owner changes {@code language:} in {@code config.yml},
 * even though players following the server default then read a different language.
 *
 * <p>Switching between a pinned language and "Server default" fires the event even when both
 * resolve to the same language, so {@link #getPreviousLanguage()} and {@link #getNewLanguage()} may
 * be equal; compare them if you only care about the text actually changing.
 *
 * @since 1.10
 */
@Getter
public final class PlayerLanguageChangeEvent extends XPrisonEvent {

	private static final HandlerList handlers = new HandlerList();

	private final UUID uuid;

	private final String previousLanguage;

	private final String newLanguage;

	private final boolean followingServerDefault;

	/**
	 * Constructs a new PlayerLanguageChangeEvent.
	 *
	 * @param uuid                   the UUID of the affected player
	 * @param previousLanguage       the code of the language the player read before the change
	 * @param newLanguage            the code of the language the player reads now
	 * @param followingServerDefault {@code true} when the player now follows the server default
	 *                               instead of a language they pinned
	 */
	public PlayerLanguageChangeEvent(UUID uuid, String previousLanguage, String newLanguage, boolean followingServerDefault) {
		this.uuid = uuid;
		this.previousLanguage = previousLanguage;
		this.newLanguage = newLanguage;
		this.followingServerDefault = followingServerDefault;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
