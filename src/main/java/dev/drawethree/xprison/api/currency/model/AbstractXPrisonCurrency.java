package dev.drawethree.xprison.api.currency.model;

import dev.drawethree.xprison.api.currency.enums.LostCause;
import dev.drawethree.xprison.api.currency.event.PlayerCurrencyLoseEvent;
import dev.drawethree.xprison.api.currency.event.PlayerCurrencyReceiveEvent;
import dev.drawethree.xprison.api.currency.enums.ReceiveCause;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Abstract implementation of {@link XPrisonCurrency} that handles event firing for balance changes.
 * <p>
 * Subclasses must implement storage operations: {@link #doAddBalance(OfflinePlayer, double, ReceiveCause)}},
 * {@link #doRemoveBalance(OfflinePlayer, double,LostCause)}, and {@link #getBalance(OfflinePlayer)}.
 */
public abstract class AbstractXPrisonCurrency implements XPrisonCurrency {

    @Override
    public final boolean addBalance(OfflinePlayer player, double amount, ReceiveCause receiveCause) {
        if (receiveCause == null) {
            receiveCause = ReceiveCause.UNKNOWN;
        }
        PlayerCurrencyReceiveEvent event = new PlayerCurrencyReceiveEvent(this, receiveCause,player, (long) amount);
        Events.callSync(event);

        if (!event.isCancelled() && event.getAmount() > 0) {
            return doAddBalance(player, event.getAmount(), event.getCause());
        }
        return false;
    }

    @Override
    public final boolean removeBalance(OfflinePlayer player, double amount, LostCause lostCause) {
        if (lostCause == null) {
            lostCause = LostCause.UNKNOWN;
        }
        PlayerCurrencyLoseEvent event = new PlayerCurrencyLoseEvent(this,lostCause, player, (long) amount);
        Events.callSync(event);

        if (event.getAmount() > 0) {
            return doRemoveBalance(player, event.getAmount(), event.getCause());
        }
        return false;
    }

    /**
     * Internal logic for adding currency to a player. Must be implemented by subclasses.
     *
     * @param player Player to add currency to
     * @param amount Final amount to add (after event modifications)
     * @param receiveCause receive reason {@link ReceiveCause}
     */
    protected abstract boolean doAddBalance(OfflinePlayer player, double amount, ReceiveCause receiveCause);

    /**
     * Internal logic for removing currency from a player. Must be implemented by subclasses.
     *
     * @param player Player to remove currency from
     * @param amount Final amount to remove (after event modifications)
     * @param lostCause lost reason {@link ReceiveCause}
     */
    protected abstract boolean doRemoveBalance(OfflinePlayer player, double amount, LostCause lostCause);
}
