package dev.drawethree.xprison.api.lostitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Safe item delivery backed by the Lost Items storage ({@code /lostitems}).
 * <p>
 * Use it instead of {@code player.getInventory().addItem(...)} whenever an item must not be lost:
 * whatever does not fit in the player's inventory is kept in their Lost Items storage, and an item
 * for an offline player is stored until they claim it.
 * <p>
 * This API is always available, even when the Lost Items module is turned off in
 * {@code config.yml}. In that case {@link #give} drops whatever does not fit at the player's feet,
 * {@link #deliver} cannot reach offline players, and {@link #isStorageEnabled()} returns
 * {@code false}.
 *
 * @since 1.10
 */
public interface XPrisonLostItemsAPI {

	/**
	 * Gives an item to an online player. Whatever does not fit in their inventory goes to their
	 * Lost Items storage (or is dropped at their feet when the storage is turned off).
	 * <p>
	 * Safe to call from any thread; the inventory is always changed on the main thread.
	 *
	 * @param player the player
	 * @param item   the item; it is copied, so the caller may reuse it
	 * @param source a short id of what gave the item (e.g. {@code "pets"}), shown in the menu and used
	 *               by its filter; lower-cased and cut to 64 characters
	 */
	void give(@NotNull Player player, @NotNull ItemStack item, @NotNull String source);

	/**
	 * Delivers an item to a player who may be offline. An online player receives it as with
	 * {@link #give}; for an offline player it is stored until they claim it.
	 *
	 * @param player the player's UUID
	 * @param item   the item; it is copied, so the caller may reuse it
	 * @param source a short id of what gave the item, as in {@link #give}
	 * @return {@code false} only if the player is offline and the Lost Items storage is turned off -
	 * the item was not delivered and the caller must keep or refund it
	 */
	boolean deliver(@NotNull UUID player, @NotNull ItemStack item, @NotNull String source);

	/**
	 * Tells whether the Lost Items storage is running.
	 *
	 * @return {@code true} if overflow and offline items are stored, {@code false} if the module is
	 * turned off
	 */
	boolean isStorageEnabled();
}
