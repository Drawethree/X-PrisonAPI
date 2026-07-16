package dev.drawethree.xprison.api.virtualblocks;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Default {@link XPrisonVirtualBlocksAPI} — a stateless delegate to the static
 * {@link VirtualBlockProviders} registry. Internal; obtain it via
 * {@link dev.drawethree.xprison.api.XPrisonAPI#getVirtualBlocksApi()}.
 */
public final class XPrisonVirtualBlocksAPIImpl implements XPrisonVirtualBlocksAPI {

	public static final XPrisonVirtualBlocksAPIImpl INSTANCE = new XPrisonVirtualBlocksAPIImpl();

	private XPrisonVirtualBlocksAPIImpl() {
	}

	@Override
	public void registerProvider(@NotNull VirtualBlockProvider provider) {
		VirtualBlockProviders.register(provider);
	}

	@Override
	public void unregisterProvider(@NotNull VirtualBlockProvider provider) {
		VirtualBlockProviders.unregister(provider);
	}

	@Override
	public boolean isVirtualMineArea(@NotNull Location location) {
		return VirtualBlockProviders.isVirtualMineArea(location);
	}

	@Override
	@Nullable
	public MineBlock blockAt(@NotNull Location location) {
		return VirtualBlockProviders.blockAt(location);
	}

	@Override
	public int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations) {
		return VirtualBlockProviders.breakBlocks(cause, locations);
	}
}
