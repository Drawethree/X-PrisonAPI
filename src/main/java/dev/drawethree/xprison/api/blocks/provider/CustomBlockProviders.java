package dev.drawethree.xprison.api.blocks.provider;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.provider.impl.ItemsAdderBlockProvider;
import dev.drawethree.xprison.api.blocks.provider.impl.NexoBlockProvider;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Registry of the {@link CustomBlockProvider}s known to X-Prison, and the single entry point through
 * which the mine block factory and mine-reset code resolve, place and remove custom blocks.
 * <p>
 * Providers are held in an <b>ordered</b> list: more specific providers come first so a
 * prefix-claiming provider (Nexo, and later Oraxen) is consulted before the {@link
 * ItemsAdderBlockProvider} catch-all for colon ids. Adding a further provider is therefore additive —
 * insert one entry ahead of ItemsAdder here; the factory and listeners need no changes.
 * <p>
 * Constructing the provider instances does not load any vendor API type (each provider confines its
 * vendor calls to methods guarded by {@link CustomBlockProvider#isEnabled()}), so this registry is
 * safe to initialise on servers that run none of the supported custom-content plugins.
 */
public final class CustomBlockProviders {

	private static final List<CustomBlockProvider> PROVIDERS = List.of(
			new NexoBlockProvider(),
			new ItemsAdderBlockProvider()
	);

	private CustomBlockProviders() {
	}

	/**
	 * @return the ordered, unmodifiable list of registered providers
	 */
	public static List<CustomBlockProvider> all() {
		return PROVIDERS;
	}

	/**
	 * Resolves a configured id to a custom {@link MineBlock} via the first provider that owns it.
	 * Does not require the owning plugin to be enabled (this mirrors config parsing, where a block may
	 * be configured before its plugin has loaded).
	 *
	 * @param configId the configured id
	 * @return the {@link MineBlock}, or {@code null} if no provider owns the id
	 */
	public static MineBlock fromConfigId(String configId) {
		for (CustomBlockProvider provider : PROVIDERS) {
			if (provider.ownsConfigId(configId)) {
				return provider.fromConfigId(configId);
			}
		}
		return null;
	}

	/**
	 * Resolves a live item to a custom {@link MineBlock} via the first enabled provider that recognises it.
	 *
	 * @param item the item
	 * @return the {@link MineBlock}, or {@code null} if no enabled provider recognises the item
	 */
	public static MineBlock fromItemStack(ItemStack item) {
		for (CustomBlockProvider provider : PROVIDERS) {
			if (provider.isEnabled()) {
				MineBlock block = provider.fromItemStack(item);
				if (block != null) {
					return block;
				}
			}
		}
		return null;
	}

	/**
	 * Resolves a placed block to a custom {@link MineBlock} via the first enabled provider that recognises it.
	 *
	 * @param block the placed block
	 * @return the {@link MineBlock}, or {@code null} if no enabled provider recognises the block
	 */
	public static MineBlock fromBlock(Block block) {
		for (CustomBlockProvider provider : PROVIDERS) {
			if (provider.isEnabled()) {
				MineBlock mineBlock = provider.fromBlock(block);
				if (mineBlock != null) {
					return mineBlock;
				}
			}
		}
		return null;
	}

	/**
	 * Places the custom block for a configured id using the owning, enabled provider.
	 *
	 * @param configId the configured id
	 * @param location the location to place at
	 */
	public static void place(String configId, Location location) {
		for (CustomBlockProvider provider : PROVIDERS) {
			if (provider.ownsConfigId(configId) && provider.isEnabled()) {
				provider.placeBlock(configId, location);
				return;
			}
		}
	}

	/**
	 * Removes any custom block present at a placed block, trying each enabled provider in turn.
	 *
	 * @param block the placed block
	 * @return {@code true} if a provider removed a custom block there
	 */
	public static boolean removeAny(Block block) {
		for (CustomBlockProvider provider : PROVIDERS) {
			if (provider.isEnabled() && provider.removeBlock(block)) {
				return true;
			}
		}
		return false;
	}
}
