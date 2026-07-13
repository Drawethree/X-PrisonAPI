package dev.drawethree.xprison.api.currency.event;

import java.math.BigDecimal;

/**
 * Internal helper shared by the currency events for the (few) places they cross the {@code double}
 * boundary. The events store the amount as an exact {@link BigDecimal}; this converts a legacy
 * {@code double} amount into that form using its canonical string ({@link BigDecimal#valueOf(double)},
 * not {@code new BigDecimal(double)}), guarding non-finite input so a bad caller can't poison an event.
 */
final class CurrencyAmounts {

	private CurrencyAmounts() {
	}

	static BigDecimal exact(double amount) {
		return Double.isFinite(amount) ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
	}
}
