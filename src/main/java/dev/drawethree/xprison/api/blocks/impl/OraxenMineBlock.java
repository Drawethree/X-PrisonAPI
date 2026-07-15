package dev.drawethree.xprison.api.blocks.impl;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link MineBlock} backed by an <a href="https://oraxen.com">Oraxen</a> custom item/block.
 * <p>
 * Oraxen identifiers have no namespace and therefore no colon (e.g. {@code sapphire_ore}), so a
 * configured Oraxen block is written with the {@link #PREFIX "oraxen:"} prefix
 * ({@code oraxen:sapphire_ore}) to distinguish it from a vanilla {@link org.bukkit.Material} name,
 * an ItemsAdder {@code namespace:id}, and a Nexo {@code nexo:id}. {@link #getId()} always returns the
 * prefixed form so a broken block's resolved id matches the key used in mine palettes and autosell
 * sell-price maps.
 * <p>
 * Every reference to an Oraxen API type is confined to {@link #toItemStack(int)} and only reached
 * after confirming Oraxen is enabled, so the JVM never hard-loads Oraxen on servers that do not run it.
 *
 * @see ItemsAdderMineBlock
 * @see NexoMineBlock
 * @see VanillaMineBlock
 */
public final class OraxenMineBlock implements MineBlock {

	/** Config prefix marking an Oraxen id, e.g. {@code oraxen:sapphire_ore}. */
	public static final String PREFIX = "oraxen:";

	private final String id;

	/**
	 * @param id the Oraxen id, either bare ({@code sapphire_ore}) or prefixed ({@code oraxen:sapphire_ore});
	 *           the prefix is stripped and stored internally as the bare id
	 */
	public OraxenMineBlock(String id) {
		this.id = (id != null && id.startsWith(PREFIX)) ? id.substring(PREFIX.length()) : id;
	}

	/**
	 * @return the bare Oraxen id without the {@link #PREFIX} (e.g. {@code sapphire_ore})
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
		if (id == null || id.isEmpty() || !Bukkit.getPluginManager().isPluginEnabled("Oraxen")) {
			return null;
		}

		var builder = io.th0rgal.oraxen.api.OraxenItems.getItemById(id);
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
