package dev.drawethree.xprison.api.enchants.area;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

/**
 * Everything {@link AreaBreakPipeline} needs to know to run an area break: the settings, the target
 * selection, and the policies that differ between enchants.
 * <p>
 * This exists so the pipeline can be shared by <b>composition</b> rather than inheritance. X-Prison's
 * own bundled enchants must extend an internal base class (their loader requires it), while addon
 * enchants extend {@link AreaBreakEnchant}; a single abstract base could not serve both, but both can
 * implement this interface and share one implementation of the break logic.
 * <p>
 * Addon developers normally never implement this directly — extend {@link AreaBreakEnchant}, which
 * implements it and exposes the same hooks as protected methods.
 *
 * @since 1.9
 */
public interface AreaBreakContext {

	/**
	 * @return the shared settings (event strategy, currency, caps, ...) for this break
	 */
	@NotNull
	AreaBreakSettings areaSettings();

	/**
	 * @return a human-readable name used in diagnostics (typically the enchant's raw name)
	 */
	@NotNull
	String areaDisplayName();

	/**
	 * Chooses the blocks to destroy. Return generously — the pipeline discards the origin, anything
	 * outside {@code region}, anything already air (unless a virtual block backs it), and anything
	 * beyond the configured block cap.
	 *
	 * @param player the mining player
	 * @param origin the block the player actually broke
	 * @param region the enchant-enabled region, or {@code null} if its bounds are unknown
	 * @param level  the enchantment level
	 * @return the candidate blocks; never {@code null}
	 */
	@NotNull
	List<Block> selectTargets(Player player, Block origin, @Nullable AreaBounds region, int level);

	/**
	 * Scales earnings by this enchant's prestige tier. Implementations resolve the tier from the
	 * pickaxe and apply their own multiplier curve.
	 *
	 * @param earnings the raw earnings
	 * @param pickaxe  the pickaxe that caused the break
	 * @return the scaled earnings
	 */
	@NotNull
	default BigDecimal applyAreaPrestige(@NotNull BigDecimal earnings, @Nullable ItemStack pickaxe) {
		return earnings;
	}

	/** Cosmetic hook fired once when the break starts, before anything is destroyed. */
	default void onBreakStart(Player player, Block origin, int level) {
	}

	/** Cosmetic hook fired once after the break is fully resolved and paid out. */
	default void onBreakComplete(Player player, List<Block> blocks) {
	}

	/**
	 * Runs the break, optionally deferring it behind an animation or a projectile.
	 * <p>
	 * {@code resolve} must be invoked <b>exactly once</b>, on the server thread, with the blocks to
	 * break; never invoking it silently cancels the break. Passing {@code targets} straight through
	 * is the normal case. An enchant whose affected area is only known once the effect finishes — a
	 * thrown bomb that may drift before it detonates — computes a fresh list at that point and
	 * passes that instead; the pipeline re-validates whatever it receives (region, cap, and whether
	 * the blocks still exist), so a delay is always safe.
	 * <p>
	 * The list is passed to the callback rather than held in a field on purpose: enchant instances
	 * are shared between players, so per-proc state must not live on the instance.
	 */
	default void dispatchWithEffect(Player player, Block origin, List<Block> targets, int level,
									Consumer<List<Block>> resolve) {
		resolve.accept(targets);
	}

	/** Clears one real (world) block. Virtual blocks never reach this method. */
	default void removeRealBlock(Player player, Block block) {
		block.setType(Material.AIR, true);
	}

	/**
	 * Whether the affected blocks are actually cleared.
	 * <p>
	 * Driven by the {@code removeBlocks} setting. {@code false} makes the enchant <b>reward-only</b>:
	 * the player is paid for the blocks but nothing is destroyed, no block state changes and no
	 * update packets are sent — by far the cheapest way to run an area enchant. On a packet mine a
	 * whole-region reward-only enchant is answered from the mine's running block census, so it costs
	 * O(distinct block types) no matter how large the mine is.
	 *
	 * @return {@code true} to clear the blocks
	 */
	default boolean shouldRemoveBlocks() {
		return areaSettings().removeBlocks();
	}

	/**
	 * Whether the break advances the mine's reset counter.
	 * <p>
	 * Follows {@link #shouldRemoveBlocks()}: a reward-only enchant leaves every block standing, so
	 * reporting them as broken would reset the mine while it is still full.
	 *
	 * @return {@code true} to report the break to the mine
	 */
	default boolean shouldCountTowardMines() {
		return shouldRemoveBlocks();
	}

	/** @return whether the mines module should clear the blocks too (the pipeline already did) */
	default boolean minesClearBlocks() {
		return false;
	}

	/**
	 * @return {@code true} only if this enchant destroys the <b>entire</b> enchant region, which
	 * enables the O(block types) bulk path on packet mines
	 */
	default boolean breaksEntireRegion() {
		return false;
	}
}
