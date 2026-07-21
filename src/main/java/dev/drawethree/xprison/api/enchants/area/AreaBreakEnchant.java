package dev.drawethree.xprison.api.enchants.area;

import com.google.gson.JsonObject;
import dev.drawethree.xprison.api.XPrisonAPI;
import dev.drawethree.xprison.api.enchants.model.BlockBreakEnchant;
import dev.drawethree.xprison.api.enchants.model.ChanceBasedEnchant;
import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantmentBase;
import dev.drawethree.xprison.api.utils.JsonUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Base class for mining enchantments that destroy <b>many blocks at once</b> (explosions, layers,
 * beams, tunnels, whole-mine nukes).
 * <p>
 * Breaking a set of blocks correctly on a prison server involves far more than clearing them:
 * drops must be given or auto-sold at the right Fortune multiple, earnings must be prestige-scaled
 * and paid into the right currency, the mine's reset counter must advance, the pickaxe must gain
 * blocks and experience, quests/battle pass/lucky blocks must progress, protection plugins must be
 * able to veto individual blocks, and all of it has to behave identically on a normal world mine
 * and on a packet-based ("virtual") mine where the blocks never exist server-side. All of that is
 * handled for you by {@link AreaBreakPipeline}; a subclass supplies only <i>which</i> blocks to
 * affect and, optionally, how it looks.
 *
 * <h2>Minimal implementation</h2>
 * <pre>{@code
 * public final class SphereEnchant extends AreaBreakEnchant {
 *
 *     private int radius;
 *
 *     public SphereEnchant(File configFile) {
 *         super(configFile);
 *     }
 *
 *     @Override
 *     protected void loadAreaProperties(JsonObject config) {
 *         this.radius = JsonUtils.getOptionalInt(config, "radius", 2);
 *     }
 *
 *     @Override
 *     protected List<Block> selectTargets(Player player, Block origin, AreaBounds region, int level) {
 *         List<Block> blocks = new ArrayList<>();
 *         for (int x = -radius; x <= radius; x++)
 *             for (int y = -radius; y <= radius; y++)
 *                 for (int z = -radius; z <= radius; z++)
 *                     blocks.add(origin.getRelative(x, y, z));
 *         return blocks;
 *     }
 *
 *     @Override
 *     public String getAuthor() {
 *         return "You";
 *     }
 * }
 * }</pre>
 * Returned blocks are filtered for you: anything outside the enchant-enabled region, already air
 * (and not backed by a virtual block), the origin block itself, or beyond the configured
 * {@code maxBlocks} cap is discarded before anything is broken.
 *
 * <h2>Shared configuration</h2>
 * Read automatically from the enchant's JSON — see {@link AreaBreakSettings}: {@code chance},
 * {@code countBlocksBroken}, {@code eventStrategy} (or the legacy {@code useEvents}),
 * {@code currencyToGive}, {@code message} and {@code maxBlocks}.
 *
 * <h2>Performance</h2>
 * The dominant cost of an area enchant is announcing the break — see {@link BreakEventStrategy}.
 * Enchants that clear an entire region should also override {@link #breaksEntireRegion()}: on a
 * packet mine that switches to a bulk path resolving the whole region straight from the mine
 * plugin's store in O(distinct block types), with no per-block world access and no per-block events.
 *
 * @see AreaBreakPipeline
 * @see BreakEventStrategy
 * @see AreaBounds
 * @since 1.9
 */
public abstract class AreaBreakEnchant extends XPrisonEnchantmentBase
		implements BlockBreakEnchant, ChanceBasedEnchant, AreaBreakContext {

	/** Chance, per enchant level, that this enchant triggers. */
	protected double chance;

	/** The shared area settings parsed from this enchant's JSON. */
	protected AreaBreakSettings areaSettings =
			new AreaBreakSettings(false, BreakEventStrategy.PER_BLOCK, "money", "", 0, true);

	/**
	 * @param configFile the JSON file describing this enchantment
	 */
	protected AreaBreakEnchant(File configFile) {
		super(configFile);
	}

	// ------------------------------------------------------------------
	// Configuration
	// ------------------------------------------------------------------

	/**
	 * Loads the configuration shared by every area enchant, then delegates to
	 * {@link #loadAreaProperties(JsonObject)} for this enchant's own settings.
	 * <p>
	 * Final by design — override {@link #loadAreaProperties(JsonObject)} instead.
	 *
	 * @param config the enchantment's JSON configuration
	 */
	@Override
	public final void loadCustomProperties(JsonObject config) {
		this.chance = JsonUtils.getOptionalDouble(config, "chance", 0.0D);
		this.areaSettings = AreaBreakSettings.fromJson(config, defaultEventStrategy());
		loadAreaProperties(config);
	}

	/**
	 * Reads this enchant's own settings (radius, depth, animation timings, ...).
	 * <p>
	 * Called after the shared area settings have been loaded. Default implementation does nothing.
	 *
	 * @param config the enchantment's JSON configuration
	 */
	protected void loadAreaProperties(JsonObject config) {
		// no enchant-specific settings by default
	}

	/**
	 * The strategy used when the configuration specifies none. Defaults to
	 * {@link BreakEventStrategy#PER_BLOCK}, the mode every external plugin understands.
	 *
	 * @return the fallback break-event strategy
	 */
	@NotNull
	protected BreakEventStrategy defaultEventStrategy() {
		return BreakEventStrategy.PER_BLOCK;
	}

	@Override
	public double getChanceToTrigger(int enchantLevel) {
		return this.chance * enchantLevel;
	}

	// ------------------------------------------------------------------
	// AreaBreakContext plumbing
	// ------------------------------------------------------------------

	@Override
	@NotNull
	public final AreaBreakSettings areaSettings() {
		return this.areaSettings;
	}

	@Override
	@NotNull
	public final String areaDisplayName() {
		return getRawName();
	}

	/**
	 * Scales earnings by this enchant's prestige tier, resolved from the pickaxe.
	 *
	 * @param earnings the raw earnings
	 * @param pickaxe  the pickaxe that caused the break
	 * @return the scaled earnings
	 */
	@Override
	@NotNull
	public BigDecimal applyAreaPrestige(@NotNull BigDecimal earnings, @Nullable ItemStack pickaxe) {
		if (!isPrestigeEnabled() || pickaxe == null) {
			return earnings;
		}
		int prestige;
		try {
			prestige = XPrisonAPI.getInstance().getEnchantsApi().getEnchantPrestige(pickaxe, this);
		} catch (RuntimeException enchantsUnavailable) {
			return earnings; // enchants module disabled mid-flight; pay the unscaled amount
		}
		if (prestige <= 0) {
			return earnings;
		}
		return earnings.multiply(BigDecimal.valueOf(getPrestigeMultiplier(prestige)));
	}

	// ------------------------------------------------------------------
	// Subclass hooks (documented on AreaBreakContext)
	// ------------------------------------------------------------------

	/**
	 * Chooses the blocks this enchant should destroy. The only method a subclass must implement.
	 * <p>
	 * Return generously — the pipeline discards the origin block, anything outside {@code region},
	 * anything already air (unless a virtual block backs it) and anything beyond {@code maxBlocks}.
	 * Prefer cheap arithmetic over world lookups: this runs on the server thread on every proc.
	 *
	 * @param player the mining player
	 * @param origin the block the player actually broke
	 * @param region the enchant-enabled region, or {@code null} if its bounds could not be resolved
	 * @param level  the enchantment level on the pickaxe
	 * @return the candidate blocks; never {@code null}
	 */
	@Override
	@NotNull
	public abstract List<Block> selectTargets(Player player, Block origin, @Nullable AreaBounds region, int level);

	// ------------------------------------------------------------------
	// Trigger
	// ------------------------------------------------------------------

	/**
	 * Runs the full area-break pipeline. Final by design — customise it through the hooks above.
	 *
	 * @param event        the break that triggered this enchantment
	 * @param enchantLevel the enchantment level on the pickaxe
	 */
	@Override
	public final void onBlockBreak(BlockBreakEvent event, int enchantLevel) {
		AreaBreakPipeline.execute(this, event, enchantLevel);
	}
}
