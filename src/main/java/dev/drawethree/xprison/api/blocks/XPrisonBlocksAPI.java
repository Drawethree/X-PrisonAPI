package dev.drawethree.xprison.api.blocks;

import dev.drawethree.xprison.api.blocks.factory.MineBlockFactory;
import dev.drawethree.xprison.api.blocks.factory.impl.MineBlockFactoryImpl;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * API interface for the Blocks features.
 */
public interface XPrisonBlocksAPI {

	default MineBlockFactory getMineBlockFactory() {
		return new MineBlockFactoryImpl();
	}

	/**
	 * Runs X-Prison's shared post-break pipeline for a set of blocks broken in one action.
	 * <p>
	 * This is what turns "blocks were destroyed" into everything the server derives from it: the
	 * player's blocks-broken statistic, lucky-block rewards, and the aggregate
	 * {@link dev.drawethree.xprison.api.shared.events.XPrisonBlockBreakEvent} that quests, the
	 * battle pass and block boosters consume.
	 * <p>
	 * Area enchants call this exactly once per proc when they are <b>not</b> emitting a Bukkit
	 * event per block (see {@link dev.drawethree.xprison.api.enchants.area.BreakEventStrategy}).
	 * When they do emit per-block events, X-Prison's own block listener already runs this pipeline
	 * for each of them — calling it again would double-count.
	 * <p>
	 * The default implementation does nothing, so an addon compiled against a newer API still runs
	 * against an older core (it simply loses the statistics/quest side effects).
	 *
	 * @param player            the player who broke the blocks
	 * @param blocks            the blocks that were broken
	 * @param countBlocksBroken whether the break should advance the blocks-broken statistic and
	 *                          fire the aggregate event; {@code false} runs only the lucky-block check
	 * @since 1.9
	 */
	default void handleBlockBreak(Player player, List<Block> blocks, boolean countBlocksBroken) {
		// No-op by default; the core implementation performs the real pipeline.
	}

	/**
	 * Bulk counterpart to {@link #handleBlockBreak(Player, List, boolean)} for breaks too large to
	 * enumerate block-by-block — notably packet ("virtual") mines, whose blocks have no real
	 * {@link Block} handles.
	 * <p>
	 * Advances the blocks-broken statistic by the total and fires
	 * {@link dev.drawethree.xprison.api.shared.events.XPrisonBulkBlockBreakEvent} so quests, the
	 * battle pass and boosters still progress, at O(distinct block types) instead of O(blocks).
	 * <p>
	 * The default implementation does nothing.
	 *
	 * @param player     the player who broke the blocks
	 * @param typeCounts how many blocks of each type were broken
	 * @since 1.9
	 */
	default void handleBulkBlockBreak(Player player, Map<MineBlock, Long> typeCounts) {
		// No-op by default; the core implementation performs the real pipeline.
	}
}
