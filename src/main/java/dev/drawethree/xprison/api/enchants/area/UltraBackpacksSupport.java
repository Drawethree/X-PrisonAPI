package dev.drawethree.xprison.api.enchants.area;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Class-presence-guarded bridge to the optional UltraBackpacks integration.
 * <p>
 * UltraBackpacks ships as a separate artifact, so the API deliberately does not depend on it at
 * compile time — the single entry point it needs is resolved reflectively once, and every method
 * is an inert no-op when the class is absent. This mirrors how the virtual-blocks support was
 * historically guarded in the addons.
 *
 * @since 1.9
 */
final class UltraBackpacksSupport {

	private static final Method HANDLE_BLOCKS_BROKEN = resolve();

	private UltraBackpacksSupport() {
	}

	private static Method resolve() {
		try {
			Class<?> api = Class.forName("dev.drawethree.ultrabackpacks.api.UltraBackpacksAPI");
			return api.getMethod("handleBlocksBroken", Player.class, List.class);
		} catch (ClassNotFoundException | NoSuchMethodException absent) {
			return null;
		}
	}

	/**
	 * @return {@code true} if the UltraBackpacks API is on the classpath
	 */
	static boolean isAvailable() {
		return HANDLE_BLOCKS_BROKEN != null;
	}

	/**
	 * Hands the broken blocks to UltraBackpacks, which gives the drops and clears the blocks.
	 *
	 * @param player the player who broke the blocks
	 * @param blocks the broken blocks
	 * @return {@code true} if UltraBackpacks handled the blocks; {@code false} if it is absent or
	 * the call failed, in which case the caller must handle the drops itself
	 */
	static boolean handleBlocksBroken(Player player, List<Block> blocks) {
		if (HANDLE_BLOCKS_BROKEN == null) {
			return false;
		}
		try {
			HANDLE_BLOCKS_BROKEN.invoke(null, player, blocks);
			return true;
		} catch (ReflectiveOperationException | RuntimeException failed) {
			return false;
		}
	}
}
