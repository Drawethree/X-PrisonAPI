package dev.drawethree.xprison.api.blocks.impl;

import dev.drawethree.xprison.api.blocks.MineBlock;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public final class ItemsAdderMineBlock implements MineBlock {

	private final String namespacedId;

	public ItemsAdderMineBlock(String namespacedId) {
		this.namespacedId = namespacedId;
	}

	public String getNamespacedId() {
		return namespacedId;
	}

	@Override
	public boolean isVanilla() {
		return false;
	}

	@Override
	public String getId() {
		return namespacedId;
	}

	@Override
	public ItemStack toItemStack(int amount) {
		if (namespacedId == null || namespacedId.isEmpty()
				|| !Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
			return null;
		}

		CustomStack customStack = CustomStack.getInstance(namespacedId);
		if (customStack != null) {
			ItemStack itemStack = customStack.getItemStack();
			itemStack.setAmount(Math.max(1, amount));
			return itemStack;
		}

		return null;
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
