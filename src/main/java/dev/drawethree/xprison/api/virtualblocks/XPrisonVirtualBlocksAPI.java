package dev.drawethree.xprison.api.virtualblocks;

import dev.drawethree.xprison.api.blocks.MineBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * API interface for the virtual (packet-only) blocks integration.
 * <p>
 * Instance facade over the {@link VirtualBlockProviders} registry, for API symmetry with the other
 * modules; packet-mines plugins may equally use the static registry directly.
 */
public interface XPrisonVirtualBlocksAPI {

	/**
	 * Registers a provider of virtual mine blocks.
	 *
	 * @param provider the provider to register
	 */
	void registerProvider(@NotNull VirtualBlockProvider provider);

	/**
	 * Unregisters a previously registered provider.
	 *
	 * @param provider the provider to remove
	 */
	void unregisterProvider(@NotNull VirtualBlockProvider provider);

	/**
	 * @param location the location to test
	 * @return {@code true} if the location lies inside any registered provider's packet-based
	 * mining region
	 */
	boolean isVirtualMineArea(@NotNull Location location);

	/**
	 * @param location the location to query
	 * @return the virtual block at the location, or {@code null} if none
	 */
	@Nullable
	MineBlock blockAt(@NotNull Location location);

	/**
	 * Removes virtual blocks through the registered providers. See
	 * {@link VirtualBlockProvider#breakBlocks(Player, Collection)} for the removal contract.
	 *
	 * @param cause     the player that caused the removal, or {@code null}
	 * @param locations the locations to remove
	 * @return the total number of blocks actually removed
	 */
	int breakBlocks(@Nullable Player cause, @NotNull Collection<Location> locations);
}
