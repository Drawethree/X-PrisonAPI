package dev.drawethree.xprison.api.pickaxelevels.event;

import dev.drawethree.xprison.api.pickaxelevels.model.PickaxeLevel;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player levels up their pickaxe.
 * <p>
 * This event is cancellable; cancelling it will prevent the level-up from being applied.
 */
@Getter
public final class PlayerPickaxeLevelUpEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Setter
    private boolean cancelled;

    /**
     * The player whose pickaxe level is being upgraded.
     */
    private final Player player;

    /**
     * The previous pickaxe level.
     */
    private final PickaxeLevel oldLevel;

    /**
     * The new pickaxe level.
     * Can be modified by listeners.
     */
    @Setter
    private PickaxeLevel newLevel;

    /**
     * Constructs a new PlayerPickaxeLevelUpEvent.
     *
     * @param player   the player whose pickaxe is leveling up
     * @param oldLevel the previous pickaxe level
     * @param newLevel the new pickaxe level
     */
    public PlayerPickaxeLevelUpEvent(Player player, PickaxeLevel oldLevel, PickaxeLevel newLevel) {
        super(player);
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Gets the handlers for this event.
     *
     * @return the handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}