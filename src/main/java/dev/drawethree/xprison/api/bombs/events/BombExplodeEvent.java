package dev.drawethree.xprison.api.bombs.events;

import dev.drawethree.xprison.api.bombs.model.Bomb;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Event fired when a {@link Bomb} explodes.
 * <p>
 * This event is called right before the explosion effects are applied, allowing
 * plugins to modify or cancel the explosion.
 */
public final class BombExplodeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link Bomb} instance that triggered the event.
     */
    @Getter
    private final Bomb bomb;

    /**
     * The {@link Player} who activated or placed the bomb.
     */
    @Getter
    private final Player player;

    /**
     * The {@link Location} at which the bomb exploded.
     */
    @Getter
    private final Location location;

    /**
     * The list of {@link Block}s that will be affected by the explosion.
     * This list can be modified before the event completes.
     */
    @Getter
    @Setter
    private List<Block> affectedBlocks;

    /**
     * Whether the event is cancelled.
     */
    private boolean cancelled;

    /**
     * Constructs a new {@code BombExplodeEvent}.
     *
     * @param bomb           the bomb that exploded
     * @param player         the player who used the bomb
     * @param location       the location where the bomb exploded
     * @param affectedBlocks the list of blocks affected by the explosion
     */
    public BombExplodeEvent(Bomb bomb, Player player, Location location, List<Block> affectedBlocks) {
        this.bomb = bomb;
        this.player = player;
        this.location = location;
        this.affectedBlocks = affectedBlocks;
    }

    /**
     * Gets the static handler list for this event.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
