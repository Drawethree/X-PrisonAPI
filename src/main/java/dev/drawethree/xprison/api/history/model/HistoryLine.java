package dev.drawethree.xprison.api.history.model;

import org.bukkit.OfflinePlayer;

import java.util.Date;

/**
 * Represents a historical record line related to a specific module and context
 * for a particular offline player. This interface defines the basic metadata
 * associated with such a history entry.
 */
public interface HistoryLine {

    /**
     * Gets the offline player associated with this history line.
     *
     * @return the {@link OfflinePlayer} this history line pertains to
     */
    OfflinePlayer getOfflinePlayer();

    /**
     * Gets the name of the module that generated or is associated with this history line.
     *
     * @return the module name as a {@link String}
     */
    String getModule();

    /**
     * Gets the context or description of this history line.
     * This can be used to give more specific details about the event or data recorded.
     *
     * @return the context as a {@link String}
     */
    String getContext();

    /**
     * Gets the date and time when this history line was created.
     *
     * @return the creation date as a {@link Date}
     */
    Date getCreatedAt();
}