package dev.drawethree.xprison.api.blocks.factory.impl;

import com.cryptomorin.xseries.XMaterial;
import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.drawethree.xprison.api.blocks.factory.MineBlockFactory;
import dev.drawethree.xprison.api.blocks.impl.ItemsAdderMineBlock;
import dev.drawethree.xprison.api.blocks.impl.VanillaMineBlock;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class MineBlockFactoryImpl implements MineBlockFactory {

	@Override
	public MineBlock fromVanilla(XMaterial material) {
		return new VanillaMineBlock(material);
	}

	@Override
	public MineBlock fromCustom(String id) {
		return new ItemsAdderMineBlock(id);
	}

	@Override
	public MineBlock fromItemStack(ItemStack itemStack) {
		if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) {
			throw new IllegalArgumentException("ItemStack cannot be null or air");
		}

		if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
			CustomStack customStack = CustomStack.byItemStack(itemStack);
			if (customStack != null) {
				return new ItemsAdderMineBlock(customStack.getNamespacedID());
			}
		}

		XMaterial material = XMaterial.matchXMaterial(itemStack);
		return new VanillaMineBlock(material);
	}

	@Override
	public MineBlock fromBlock(Block block) {
		if (block == null || block.getType() == XMaterial.AIR.parseMaterial()) {
			throw new IllegalArgumentException("Block cannot be null or air");
		}

		if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
			CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
			if (customBlock != null) {
				return new ItemsAdderMineBlock(customBlock.getNamespacedID());
			}
		}

		XMaterial material = XMaterial.matchXMaterial(block.getType());
		return new VanillaMineBlock(material);
	}

}
