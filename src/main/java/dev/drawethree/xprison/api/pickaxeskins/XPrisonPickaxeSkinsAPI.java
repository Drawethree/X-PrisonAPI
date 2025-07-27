package dev.drawethree.xprison.api.pickaxeskins;

import dev.drawethree.xprison.api.pickaxeskins.model.PickaxeSkin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * API for managing pickaxe skins in XPrison.
 * <p>
 * This interface allows external plugins to retrieve, apply, or manipulate
 * cosmetic pickaxe skins which may provide gameplay multipliers (money, tokens, gems).
 */
public interface XPrisonPickaxeSkinsAPI {

	/**
	 * Retrieves the {@link PickaxeSkin} applied to the given pickaxe item.
	 * <p>
	 * This checks for skin metadata (e.g., via CustomModelData or NBT tags) stored in the item.
	 *
	 * @param item the ItemStack representing the pickaxe
	 * @return an {@link Optional} containing the PickaxeSkin if one is applied; otherwise, empty
	 */
	Optional<PickaxeSkin> getPickaxeSkin(ItemStack item);

	/**
	 * Retrieves the {@link PickaxeSkin} currently applied to the item the player is holding.
	 *
	 * @param player the player whose held item will be checked
	 * @return an {@link Optional} containing the PickaxeSkin if found; otherwise, empty
	 */
	Optional<PickaxeSkin> getPickaxeSkin(Player player);

	/**
	 * Retrieves a {@link PickaxeSkin} by its unique identifier.
	 * <p>
	 * Skin IDs correspond to the keys defined in your configuration (e.g., "legendary", "neon").
	 *
	 * @param id the ID of the skin to fetch
	 * @return an {@link Optional} containing the PickaxeSkin if it exists; otherwise, empty
	 */
	Optional<PickaxeSkin> getPickaxeSkin(String id);

	/**
	 * Applies a {@link PickaxeSkin} to a given item for a specific player.
	 * <p>
	 * This typically modifies the item's CustomModelData or NBT to reflect the skin.
	 *
	 * @param player the player applying the skin
	 * @param item the ItemStack to modify (must be a valid pickaxe)
	 * @param skin the PickaxeSkin to apply to the item
	 */
	void setPickaxeSkin(Player player, ItemStack item, PickaxeSkin skin);
}
