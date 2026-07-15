package dev.drawethree.xprison.api.blocks.provider.impl;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.impl.OraxenMineBlock;
import dev.drawethree.xprison.api.blocks.provider.CustomBlockProvider;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.mechanics.Mechanic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * {@link CustomBlockProvider} for <a href="https://oraxen.com">Oraxen</a>.
 * <p>
 * Oraxen ids have no colon, so a configured Oraxen block is written with the {@link OraxenMineBlock#PREFIX
 * "oraxen:"} prefix. This provider owns exactly the {@code oraxen:}-prefixed ids and strips the prefix
 * before handing the bare id to the Oraxen API.
 */
public final class OraxenBlockProvider implements CustomBlockProvider {

	@Override
	public String pluginName() {
		return "Oraxen";
	}

	@Override
	public boolean isEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("Oraxen");
	}

	@Override
	public boolean ownsConfigId(String configId) {
		return configId != null && configId.startsWith(OraxenMineBlock.PREFIX);
	}

	@Override
	public MineBlock fromConfigId(String configId) {
		return new OraxenMineBlock(configId);
	}

	@Override
	public MineBlock fromItemStack(ItemStack item) {
		if (!isEnabled() || item == null) {
			return null;
		}
		String oraxenId = OraxenItems.getIdByItem(item);
		return oraxenId != null ? new OraxenMineBlock(oraxenId) : null;
	}

	@Override
	public MineBlock fromBlock(Block block) {
		if (!isEnabled() || block == null || !OraxenBlocks.isOraxenBlock(block)) {
			return null;
		}
		Mechanic mechanic = OraxenBlocks.getOraxenBlock(block.getLocation());
		if (mechanic == null) {
			return null;
		}
		String oraxenId = mechanic.getItemID();
		return oraxenId != null ? new OraxenMineBlock(oraxenId) : null;
	}

	@Override
	public void placeBlock(String configId, Location location) {
		if (!isEnabled()) {
			return;
		}
		String bareId = configId.startsWith(OraxenMineBlock.PREFIX)
				? configId.substring(OraxenMineBlock.PREFIX.length()) : configId;
		OraxenBlocks.place(bareId, location);
	}

	@Override
	public boolean removeBlock(Block block) {
		if (!isEnabled() || !OraxenBlocks.isOraxenBlock(block)) {
			return false;
		}
		return OraxenBlocks.remove(block.getLocation(), null);
	}
}
