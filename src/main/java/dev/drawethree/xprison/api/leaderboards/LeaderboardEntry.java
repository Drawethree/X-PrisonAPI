package dev.drawethree.xprison.api.leaderboards;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * One row of a leaderboard: who holds the position and with what value.
 * <p>
 * Instances are immutable. The position is not stored on the entry itself - it is the entry's
 * index in the list a {@link LeaderboardSource} returns, plus one.
 *
 * @since 1.10
 */
public final class LeaderboardEntry {

	private final UUID uuid;
	private final String name;
	private final BigDecimal value;
	private final String formattedValue;

	/**
	 * Creates a leaderboard row.
	 *
	 * @param uuid           the player holding the position, or {@code null} when the row is not a
	 *                       player (for example a gang)
	 * @param name           the name shown for the row
	 * @param value          the exact value the board is sorted by
	 * @param formattedValue the value as it should be displayed (currency format, rank prefix, ...)
	 */
	public LeaderboardEntry(@Nullable UUID uuid, @NotNull String name, @NotNull BigDecimal value, @NotNull String formattedValue) {
		this.uuid = uuid;
		this.name = Objects.requireNonNull(name, "name");
		this.value = Objects.requireNonNull(value, "value");
		this.formattedValue = Objects.requireNonNull(formattedValue, "formattedValue");
	}

	/**
	 * Gets the player holding this position.
	 *
	 * @return the player's UUID, or {@code null} if this row does not belong to a player
	 */
	@Nullable
	public UUID getUuid() {
		return this.uuid;
	}

	/**
	 * Gets the display name of this row - a player name or, for non-player boards, the name of
	 * whatever is being ranked.
	 *
	 * @return the display name
	 */
	@NotNull
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the exact value this row is ranked by.
	 *
	 * @return the raw value
	 */
	@NotNull
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * Gets the value as it should be shown to players.
	 *
	 * @return the formatted value
	 */
	@NotNull
	public String getFormattedValue() {
		return this.formattedValue;
	}
}
