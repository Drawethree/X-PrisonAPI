package dev.drawethree.xprison.api.milestones.model;

import org.jetbrains.annotations.NotNull;

/**
 * How reaching a milestone is announced: what the player is told, what the server is told, and the
 * noise it makes.
 * <p>
 * Every piece is optional - an empty string, or {@code false} for the firework, means that piece is
 * skipped. Text is raw MiniMessage and may use {@code %player%} and {@code %value%}.
 *
 * @since 1.9
 */
public interface MilestoneAnnouncement {

	/**
	 * Gets the title shown to the player.
	 *
	 * @return the title, or an empty string if none is configured
	 */
	@NotNull
	String getTitle();

	/**
	 * Gets the subtitle shown to the player.
	 *
	 * @return the subtitle, or an empty string if none is configured
	 */
	@NotNull
	String getSubtitle();

	/**
	 * Gets the chat message sent to the player.
	 *
	 * @return the message, or an empty string if none is configured
	 */
	@NotNull
	String getMessage();

	/**
	 * Gets the message broadcast to everyone online.
	 *
	 * @return the broadcast, or an empty string if none is configured
	 */
	@NotNull
	String getBroadcast();

	/**
	 * Gets the name of the sound played to the player.
	 *
	 * @return the sound name, or an empty string if none is configured
	 */
	@NotNull
	String getSound();

	/**
	 * Checks whether a firework is launched at the player.
	 *
	 * @return {@code true} if a firework is launched
	 */
	boolean isFirework();
}
