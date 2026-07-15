package dev.drawethree.xprison.api.blocks.provider;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Service-provider abstraction over a custom-content plugin (ItemsAdder, Nexo, ...) that can supply
 * custom blocks to a mine.
 * <p>
 * Implementations centralise every reference to their vendor's API so custom-content plugins become
 * pluggable: the mine block factory and the plugin's placement/removal code iterate the registered
 * providers instead of hard-coding vendor branches. Adding a new provider is therefore additive — a
 * new implementation plus one registry entry in {@link CustomBlockProviders}.
 * <p>
 * <b>Lazy loading contract:</b> {@link #ownsConfigId(String)} and {@link #fromConfigId(String)} must
 * not touch any vendor API type (they run for config parsing regardless of whether the plugin is
 * installed). Every other method must guard its vendor calls behind {@link #isEnabled()} so the JVM
 * never hard-loads a plugin that is not present on the server.
 *
 * @see CustomBlockProviders
 */
public interface CustomBlockProvider {

	/**
	 * @return the Bukkit plugin name this provider bridges (e.g. {@code "ItemsAdder"}, {@code "Nexo"})
	 */
	String pluginName();

	/**
	 * @return {@code true} if the backing plugin is installed and enabled on this server
	 */
	boolean isEnabled();

	/**
	 * Whether a configured block id belongs to this provider. Must be a pure string test that does not
	 * reference any vendor API type, so it is safe to call when the plugin is absent.
	 *
	 * @param configId the configured id (e.g. {@code "myitems:ruby_ore"}, {@code "nexo:sapphire_ore"})
	 * @return {@code true} if this provider owns the id
	 */
	boolean ownsConfigId(String configId);

	/**
	 * Creates a {@link MineBlock} from a configured id this provider {@link #ownsConfigId(String) owns}.
	 * Performs no world lookup and must not reference any vendor API type.
	 *
	 * @param configId the configured id
	 * @return a new {@link MineBlock}
	 */
	MineBlock fromConfigId(String configId);

	/**
	 * Resolves a live {@link ItemStack} to a {@link MineBlock} of this provider.
	 *
	 * @param item the item
	 * @return the {@link MineBlock}, or {@code null} if the item is not one of this provider's items
	 */
	MineBlock fromItemStack(ItemStack item);

	/**
	 * Resolves a placed {@link Block} to a {@link MineBlock} of this provider.
	 *
	 * @param block the placed block
	 * @return the {@link MineBlock}, or {@code null} if the block is not one of this provider's blocks
	 */
	MineBlock fromBlock(Block block);

	/**
	 * Places this provider's custom block at a location.
	 *
	 * @param configId the configured id (prefixed form, as used in config)
	 * @param location the location to place at
	 */
	void placeBlock(String configId, Location location);

	/**
	 * Removes this provider's custom block at a placed block, if one is present there.
	 *
	 * @param block the placed block
	 * @return {@code true} if a custom block belonging to this provider was removed
	 */
	boolean removeBlock(Block block);
}
