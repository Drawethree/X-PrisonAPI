package dev.drawethree.xprison.api.blocks.provider.impl;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.impl.NexoMineBlock;
import dev.drawethree.xprison.api.blocks.provider.CustomBlockProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * {@link CustomBlockProvider} for <a href="https://nexomc.com">Nexo</a>.
 * <p>
 * Nexo ids have no colon, so a configured Nexo block is written with the {@link NexoMineBlock#PREFIX
 * "nexo:"} prefix. This provider owns exactly the {@code nexo:}-prefixed ids and strips the prefix
 * before handing the bare id to the Nexo API.
 */
public final class NexoBlockProvider implements CustomBlockProvider {

	@Override
	public String pluginName() {
		return "Nexo";
	}

	@Override
	public boolean isEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("Nexo");
	}

	@Override
	public boolean ownsConfigId(String configId) {
		return configId != null && configId.startsWith(NexoMineBlock.PREFIX);
	}

	@Override
	public MineBlock fromConfigId(String configId) {
		return new NexoMineBlock(configId);
	}

	@Override
	public MineBlock fromItemStack(ItemStack item) {
		if (!isEnabled() || item == null) {
			return null;
		}
		String nexoId = NexoItems.idFromItem(item);
		return nexoId != null ? new NexoMineBlock(nexoId) : null;
	}

	@Override
	public MineBlock fromBlock(Block block) {
		if (!isEnabled() || block == null || !NexoBlocks.isCustomBlock(block)) {
			return null;
		}
		CustomBlockMechanic mechanic = NexoBlocks.customBlockMechanic(block);
		if (mechanic == null) {
			return null;
		}
		String nexoId = mechanic.getItemID();
		return nexoId != null ? new NexoMineBlock(nexoId) : null;
	}

	@Override
	public void placeBlock(String configId, Location location) {
		if (!isEnabled()) {
			return;
		}
		String bareId = configId.startsWith(NexoMineBlock.PREFIX)
				? configId.substring(NexoMineBlock.PREFIX.length()) : configId;
		NexoBlocks.place(bareId, location);
	}

	@Override
	public boolean removeBlock(Block block) {
		if (!isEnabled() || !NexoBlocks.isCustomBlock(block)) {
			return false;
		}
		return NexoBlocks.remove(block.getLocation());
	}
}
