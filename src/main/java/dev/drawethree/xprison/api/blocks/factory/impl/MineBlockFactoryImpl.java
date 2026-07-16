package dev.drawethree.xprison.api.blocks.factory.impl;

import com.cryptomorin.xseries.XMaterial;
import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.factory.MineBlockFactory;
import dev.drawethree.xprison.api.blocks.impl.VanillaMineBlock;
import dev.drawethree.xprison.api.blocks.provider.CustomBlockProviders;
import dev.drawethree.xprison.api.virtualblocks.VirtualBlockProviders;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Default {@link MineBlockFactory}. Custom-block resolution is delegated to the ordered
 * {@link CustomBlockProviders} registry (ItemsAdder, Nexo, ...); anything no provider claims falls
 * back to a {@link VanillaMineBlock}. Adding a new custom-content provider therefore requires no
 * change here.
 */
public class MineBlockFactoryImpl implements MineBlockFactory {

	@Override
	public MineBlock fromVanilla(XMaterial material) {
		return new VanillaMineBlock(material);
	}

	@Override
	public MineBlock fromCustom(String id) {
		return CustomBlockProviders.fromConfigId(id);
	}

	@Override
	public MineBlock fromItemStack(ItemStack itemStack) {
		if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) {
			throw new IllegalArgumentException("ItemStack cannot be null or air");
		}

		MineBlock custom = CustomBlockProviders.fromItemStack(itemStack);
		if (custom != null) {
			return custom;
		}

		return new VanillaMineBlock(XMaterial.matchXMaterial(itemStack));
	}

	@Override
	public MineBlock fromId(String id) {
		if (id == null || id.isBlank()) return null;

		MineBlock custom = CustomBlockProviders.fromConfigId(id);
		if (custom != null) {
			return custom;
		}

		return XMaterial.matchXMaterial(id.toUpperCase())
				.map(VanillaMineBlock::new)
				.orElse(null);
	}

	@Override
	public MineBlock fromBlock(Block block) {
		if (block == null) {
			throw new IllegalArgumentException("Block cannot be null or air");
		}

		if (block.getType() == XMaterial.AIR.parseMaterial()) {
			// Server-side air may still be a virtual (packet-only) mine block; resolve it through
			// the registered providers / open snapshots before rejecting the block.
			MineBlock virtual = VirtualBlockProviders.blockAt(block.getLocation());
			if (virtual != null) {
				return virtual;
			}
			throw new IllegalArgumentException("Block cannot be null or air");
		}

		MineBlock custom = CustomBlockProviders.fromBlock(block);
		if (custom != null) {
			return custom;
		}

		return new VanillaMineBlock(XMaterial.matchXMaterial(block.getType()));
	}

}
