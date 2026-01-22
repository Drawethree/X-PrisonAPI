package dev.drawethree.xprison.api.bombs.events;

import dev.drawethree.xprison.api.bombs.model.Bomb;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.*;

/**
 * {@code BombExplodeEvent} is fired whenever a {@link Bomb} explosion occurs.
 * <p>
 * This event is {@link Cancellable} and is designed to allow multiple plugins to
 * collaboratively determine which blocks should be affected by the explosion.
 * </p>
 *
 * <h2>Event Workflow</h2>
 * <ol>
 *   <li>The event is constructed with the {@link #originalBlocks} list containing
 *       <b>all</b> blocks that would be affected by the explosion.</li>
 *   <li>When the event is fired, {@link #approved} starts empty — no blocks are initially approved.</li>
 *   <li>Each listener inspects {@link #getOriginalBlocks()} and uses {@link #addAffectedBlocks(Collection)}
 *       to approve blocks for removal. Approved blocks from all listeners are <b>merged</b> into a single set.</li>
 *   <li>After all listeners have run, {@link #getAffectedBlocks()} contains the
 *       <b>union</b> of blocks approved by all listeners.</li>
 *   <li>The plugin firing this event should use {@link #getAffectedBlocks()} to
 *       perform the actual block modifications (e.g., setting them to air).</li>
 * </ol>
 *
 * <p>This approach prevents listeners from overwriting each other's decisions,
 * avoiding the common "last listener wins" problem with lists.</p>
 *
 * <h2>Listener Guidelines</h2>
 * <ul>
 *   <li>Use {@link #getOriginalBlocks()} to examine the full set of possible blocks.</li>
 *   <li>Call {@link #addAffectedBlocks(Collection)} to approve blocks your plugin owns.</li>
 *   <li>Avoid calling {@link #setAffectedBlocks(List)} unless you explicitly intend to
 *       replace all previously approved blocks.</li>
 *   <li>Check {@link #isCancelled()} before doing heavy processing if you want to
 *       ignore cancelled explosions.</li>
 * </ul>
 */
public final class BombExplodeEvent extends XPrisonPlayerEvent implements Cancellable {

    /** Required Bukkit handler list for all custom events. */
    private static final HandlerList handlers = new HandlerList();

    /** The bomb that triggered this event. */
    private final Bomb bomb;

    /** The player who placed or triggered the bomb. */
    private final Player player;

    /** The location at which the bomb exploded. */
    private final Location location;

    /**
     * An immutable list of all blocks that would be affected by the explosion
     * before any listener filtering.
     * <p>
     * This list is provided for reference and inspection only — it will never change
     * during the event lifecycle.
     * </p>
     */
    @Getter
    private final List<Block> originalBlocks;

    /**
     * The set of blocks that have been approved for removal by listeners.
     * <p>
     * Implemented as a {@link LinkedHashSet} to preserve insertion order and prevent duplicates.
     * </p>
     */
    private final Set<Block> approved = new LinkedHashSet<>();

    /** Whether this event has been cancelled. */
    private boolean cancelled;

    /**
     * Constructs a new {@code BombExplodeEvent}.
     *
     * @param bomb           the bomb that triggered the explosion
     * @param player         the player responsible for triggering the bomb
     * @param location       the location of the explosion
     * @param affectedBlocks the initial list of all blocks that would be affected
     *                       by the explosion before listener filtering
     */
    public BombExplodeEvent(Bomb bomb, Player player, Location location, List<Block> affectedBlocks) {
        super(player);
        this.bomb = bomb;
        this.player = player;
        this.location = location;
        this.originalBlocks = Collections.unmodifiableList(new ArrayList<>(affectedBlocks));
    }

    /**
     * Returns the blocks that listeners have approved for removal.
     * <p>
     * This list is the union of all approvals made by listeners and is built up
     * via calls to {@link #addAffectedBlocks(Collection)} or {@link #setAffectedBlocks(List)}.
     * </p>
     *
     * @return a new modifiable {@link List} of approved blocks
     */
    public List<Block> getAffectedBlocks() {
        return new ArrayList<>(approved);
    }

    /**
     * Adds blocks to the approved set.
     * <p>
     * Only blocks that are present in {@link #getOriginalBlocks()} will be added;
     * any blocks not part of the original explosion area are ignored for safety.
     * </p>
     *
     * @param blocks the collection of blocks to approve
     */
    public void addAffectedBlocks(Collection<Block> blocks) {
        for (Block b : blocks) {
            if (originalBlocks.contains(b)) {
                approved.add(b);
            }
        }
    }

    /**
     * Adds block to the approved set.
     * <p>
     * Only block that is present in {@link #getOriginalBlocks()} will be added;
     * any blocks not part of the original explosion area are ignored for safety.
     * </p>
     *
     * @param block block to approve
     */
    public void addAffectedBlock(Block block) {
        if (originalBlocks.contains(block)) {
            approved.add(block);
        }
    }

    /**
     * Replaces the approved set with the provided blocks.
     * <p>
     * This method clears all previously approved blocks and then approves the
     * specified ones. Like {@link #addAffectedBlocks(Collection)}, only blocks
     * from {@link #getOriginalBlocks()} are accepted.
     * </p>
     * <p>
     * <b>Warning:</b> Calling this will overwrite any approvals made by other listeners.
     * Use sparingly — {@link #addAffectedBlocks(Collection)} is generally safer.
     * </p>
     *
     * @param blocks the new list of approved blocks
     */
    public void setAffectedBlocks(List<Block> blocks) {
        approved.clear();
        addAffectedBlocks(blocks);
    }

    /**
     * Required Bukkit method to retrieve the static handler list.
     *
     * @return the {@link HandlerList} for this event type
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Required Bukkit method to retrieve the handler list for this instance.
     *
     * @return the {@link HandlerList} for this event type
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets whether this event is cancelled.
     *
     * @return {@code true} if the event is cancelled, {@code false} otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancelled {@code true} to cancel the event, {@code false} to allow it
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the {@link Bomb} that triggered this event.
     *
     * @return the bomb instance
     */
    public Bomb getBomb() {
        return bomb;
    }

    /**
     * Gets the {@link Player} who triggered the bomb.
     *
     * @return the player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the location of the explosion.
     *
     * @return the explosion {@link Location}
     */
    public Location getLocation() {
        return location;
    }
}
