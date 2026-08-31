package dev.drawethree.xprison.api.enchants.area;

import me.lucko.helper.Schedulers;
import org.jetbrains.annotations.NotNull;

/**
 * Decides when an area break is too large to finish in a single tick, and supplies the seam it is
 * spread over.
 *
 * <h2>Why</h2>
 * {@link AreaBreakPipeline} completes a proc in one synchronous pass. A per-block cap of a few
 * hundred blocks is harmless there, but a whole-mine enchant configured with a five-figure cap
 * clears every one of those blocks — plus the mine's reset accounting and the aggregate break
 * event — inside a single tick. One such proc is survivable; two hundred miners procing them is a
 * measurable TPS loss, which is exactly the budget X-Prison's mining hot path is held to.
 * <p>
 * Above {@link #threshold()} the pipeline therefore clears the blocks, advances the mine and fires
 * the aggregate event in slices of at most that many blocks, one slice per tick. Everything a
 * player can observe as a reward — auto-sell earnings, the currency payout, the proc message,
 * pickaxe blocks and experience — is still resolved once, synchronously, before the first slice
 * runs, so spreading the work can neither duplicate nor strand a payout.
 * <p>
 * At or below the threshold nothing changes: the proc takes the original single-pass path.
 *
 * <h2>Tuning</h2>
 * The default of {@value #DEFAULT_THRESHOLD} blocks is comfortably above every stock X-Prison
 * enchant's cap, so a default installation never chunks. Only a deliberately huge cap — a Nuke or
 * Layer tuned to clear a whole mine — crosses it.
 *
 * @since 1.9
 */
public final class AreaBreakChunking {

	/** The default block count above which a proc is spread over several ticks. */
	public static final int DEFAULT_THRESHOLD = 1500;

	private static final TickScheduler DEFAULT_SCHEDULER = slice -> Schedulers.sync().runLater(slice, 1L);

	private static volatile int threshold = DEFAULT_THRESHOLD;

	private static volatile TickScheduler scheduler = DEFAULT_SCHEDULER;

	private AreaBreakChunking() {
	}

	/**
	 * Runs a slice of a spread-out area break on the server thread, on a later tick.
	 * <p>
	 * The default implementation defers by one tick through helper's scheduler, which X-Prison
	 * already hard-depends on. A host that schedules differently — or a test that wants to drive the
	 * slices by hand — replaces it through {@link #setScheduler(TickScheduler)}.
	 *
	 * @since 1.9
	 */
	@FunctionalInterface
	public interface TickScheduler {

		/**
		 * Schedules one slice.
		 *
		 * @param slice the work to run on the server thread on a later tick
		 * @throws RuntimeException if scheduling is impossible right now (a disabling plugin, a
		 *                          server that is shutting down); the pipeline then finishes the
		 *                          remaining slices inline rather than losing them
		 */
		void runNextTick(@NotNull Runnable slice);
	}

	/**
	 * @return the block count above which a proc is spread over several ticks; {@code 0} or less
	 * means chunking is off and every proc runs in one pass
	 */
	public static int threshold() {
		return threshold;
	}

	/**
	 * Sets the block count above which a proc is spread over several ticks, and the size of each
	 * slice.
	 *
	 * @param blocks the threshold in blocks; {@code 0} or less disables chunking entirely
	 */
	public static void setThreshold(int blocks) {
		threshold = blocks;
	}

	/**
	 * @param blockCount the number of blocks a proc is about to process
	 * @return {@code true} if the proc must be spread over several ticks
	 */
	public static boolean shouldChunk(int blockCount) {
		int limit = threshold;
		return limit > 0 && blockCount > limit;
	}

	/**
	 * Replaces the scheduler slices are deferred through.
	 *
	 * @param tickScheduler the scheduler to use; {@code null} restores the default
	 */
	public static void setScheduler(TickScheduler tickScheduler) {
		scheduler = tickScheduler == null ? DEFAULT_SCHEDULER : tickScheduler;
	}

	/**
	 * Attempts to defer one slice to a later tick.
	 * <p>
	 * Never throws: when the platform cannot schedule (helper missing, server shutting down) the
	 * caller is told so and runs the slice inline instead, trading the tick spread for never
	 * dropping work that has already been paid for.
	 *
	 * @param slice the work to defer
	 * @return {@code true} if the slice was scheduled, {@code false} if the caller must run it itself
	 */
	static boolean tryRunNextTick(@NotNull Runnable slice) {
		try {
			scheduler.runNextTick(slice);
			return true;
		} catch (RuntimeException | LinkageError cannotSchedule) {
			return false;
		}
	}
}
