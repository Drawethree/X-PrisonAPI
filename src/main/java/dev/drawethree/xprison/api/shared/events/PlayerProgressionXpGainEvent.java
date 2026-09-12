package dev.drawethree.xprison.api.shared.events;

import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired whenever a player is about to gain experience in a progression system that is
 * <em>not</em> part of the X-Prison core - an addon or companion plugin such as the armor
 * progression of XPrisonArmors.
 * <p>
 * Each progression identifies itself with a namespaced key (for example
 * {@code xprisonarmors:armor}) so that boosters like the Events addon can target a single
 * progression, several of them or all of them without depending on the plugin that owns it.
 * <p>
 * The {@link #getAmount() amount} is the value the owning plugin is about to award after its own
 * modifiers. Listeners may scale it via {@link #setAmount(long)} (values {@code <= 0} suppress
 * the gain) or cancel the event to prevent the gain entirely. The event is fired synchronously
 * on the main thread.
 *
 * @since 1.10
 */
@Getter
public final class PlayerProgressionXpGainEvent extends XPrisonPlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Setter
    private boolean cancelled;

    /**
     * The player gaining the experience.
     */
    private final Player player;

    /**
     * The namespaced key of the progression awarding the experience, e.g. {@code xprisonarmors:armor}.
     */
    private final String progression;

    /**
     * The amount of experience about to be awarded. Listeners may modify it; values
     * {@code <= 0} suppress the gain.
     */
    @Setter
    private long amount;

    /**
     * Constructs a new {@link PlayerProgressionXpGainEvent}.
     *
     * @param player      the player gaining the experience
     * @param progression the namespaced key of the progression awarding it
     * @param amount      the amount about to be awarded
     */
    public PlayerProgressionXpGainEvent(@NotNull Player player, @NotNull String progression, long amount) {
        super(player);
        this.player = player;
        this.progression = progression;
        this.amount = amount;
    }

    /**
     * Returns the static handler list for this event.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Returns the handler list for this event instance.
     *
     * @return the handler list
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
