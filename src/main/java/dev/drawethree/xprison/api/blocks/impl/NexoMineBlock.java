package dev.drawethree.xprison.api.blocks.impl;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link MineBlock} backed by a <a href="https://nexomc.com">Nexo</a> custom item/block.
 * <p>
 * Nexo identifiers have no namespace and therefore no colon (e.g. {@code sapphire_ore}), so a
 * configured Nexo block is written with the {@link #PREFIX "nexo:"} prefix ({@code nexo:sapphire_ore})
 * to distinguish it from a vanilla {@link org.bukkit.Material} name and from an ItemsAdder
 * {@code namespace:id}. {@link #getId()} always returns the prefixed form so a broken block's
 * resolved id matches the key used in mine palettes and autosell sell-price maps.
 * <p>
 * Every reference to a Nexo API type is confined to {@link #toItemStack(int)} and only reached after
 * confirming Nexo is enabled, so the JVM never hard-loads Nexo on servers that do not run it.
 *
 * @see ItemsAdderMineBlock
 * @see VanillaMineBlock
 */
public final class NexoMineBlock implements MineBlock {

	/** Config prefix marking a Nexo id, e.g. {@code nexo:sapphire_ore}. */
	public static final String PREFIX = "nexo:";

	private final String id;

	/**
	 * @param id the Nexo id, either bare ({@code sapphire_ore}) or prefixed ({@code nexo:sapphire_ore});
	 *           the prefix is stripped and stored internally as the bare id
	 */
	public NexoMineBlock(String id) {
		this.id = (id != null && id.startsWith(PREFIX)) ? id.substring(PREFIX.length()) : id;
	}

	/**
	 * @return the bare Nexo id without the {@link #PREFIX} (e.g. {@code sapphire_ore})
	 */
	public String getBareId() {
		return id;
	}

	@Override
	public boolean isVanilla() {
		return false;
	}

	@Override
	public String getId() {
		return PREFIX + id;
	}

	@Override
	public ItemStack toItemStack(int amount) {
		if (id == null || id.isEmpty() || !Bukkit.getPluginManager().isPluginEnabled("Nexo")) {
			return null;
		}

		var builder = com.nexomc.nexo.api.NexoItems.itemFromId(id);
		if (builder == null) {
			return null;
		}

		ItemStack itemStack = builder.build();
		if (itemStack == null) {
			return null;
		}
		itemStack.setAmount(Math.max(1, amount));
		return itemStack;
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
