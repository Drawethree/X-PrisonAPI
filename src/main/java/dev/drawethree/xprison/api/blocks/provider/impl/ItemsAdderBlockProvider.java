package dev.drawethree.xprison.api.blocks.provider.impl;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.impl.ItemsAdderMineBlock;
import dev.drawethree.xprison.api.blocks.provider.CustomBlockProvider;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * {@link CustomBlockProvider} for <a href="https://itemsadder.devs.beer">ItemsAdder</a>.
 * <p>
 * ItemsAdder ids are {@code namespace:id} (they contain a colon). This provider owns any colon id
 * <em>except</em> a {@code nexo:} id, so it acts as the catch-all for namespaced ids while leaving
 * Nexo (and any future colon-prefixed provider) to claim its own prefix — see
 * {@link dev.drawethree.xprison.api.blocks.provider.CustomBlockProviders}, which orders more specific
 * providers ahead of this one.
 */
public final class ItemsAdderBlockProvider implements CustomBlockProvider {

	@Override
	public String pluginName() {
		return "ItemsAdder";
	}

	@Override
	public boolean isEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
	}

	@Override
	public boolean ownsConfigId(String configId) {
		return configId != null && configId.indexOf(':') >= 0;
	}

	@Override
	public MineBlock fromConfigId(String configId) {
		return new ItemsAdderMineBlock(configId);
	}

	@Override
	public MineBlock fromItemStack(ItemStack item) {
		if (!isEnabled() || item == null) {
			return null;
		}
		CustomStack customStack = CustomStack.byItemStack(item);
		return customStack != null ? new ItemsAdderMineBlock(customStack.getNamespacedID()) : null;
	}

	@Override
	public MineBlock fromBlock(Block block) {
		if (!isEnabled() || block == null) {
			return null;
		}
		CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
		return customBlock != null ? new ItemsAdderMineBlock(customBlock.getNamespacedID()) : null;
	}

	@Override
	public void placeBlock(String configId, Location location) {
		if (!isEnabled()) {
			return;
		}
		CustomBlock customBlock = CustomBlock.getInstance(configId);
		if (customBlock != null) {
			customBlock.place(location);
		}
	}

	@Override
	public boolean removeBlock(Block block) {
		if (!isEnabled()) {
			return false;
		}
		CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
		if (customBlock != null) {
			customBlock.remove();
			return true;
		}
		return false;
	}
}
