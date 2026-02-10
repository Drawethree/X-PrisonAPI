package dev.drawethree.xprison.api.blocks.impl;

import com.cryptomorin.xseries.XMaterial;
import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.inventory.ItemStack;

public final class VanillaMineBlock implements MineBlock {

	private final XMaterial material;

	public VanillaMineBlock(XMaterial material) {
		this.material = material;
	}

	public XMaterial getMaterial() {
		return material;
	}

	@Override
	public boolean isVanilla() {
		return true;
	}

	@Override
	public String getId() {
		return material.name();
	}

	@Override
	public ItemStack toItemStack(int amount) {
		ItemStack tempItem = material.parseItem();
		tempItem.setAmount(amount);
		return tempItem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MineBlock)) return false;
		return getId().equals(((MineBlock) o).getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
