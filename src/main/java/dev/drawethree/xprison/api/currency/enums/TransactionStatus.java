package dev.drawethree.xprison.api.currency.enums;

/**
 * Result of a currency balance operation.
 * <p>
 * Returned by the {@code try*} methods on
 * {@link dev.drawethree.xprison.api.currency.XPrisonCurrencyAPI} so callers can react to the exact
 * reason an operation succeeded or failed, instead of a bare {@code boolean}.
 */
public enum TransactionStatus {

    /** The operation completed successfully. */
    SUCCESS,

    /** The player did not have enough balance for the requested removal/transfer. */
    INSUFFICIENT_FUNDS,

    /** No currency with the supplied name is registered. */
    INVALID_CURRENCY,

    /** The supplied player could not be resolved. */
    PLAYER_NOT_FOUND,

    /** A {@link dev.drawethree.xprison.api.shared.events.XPrisonEvent} cancelled the operation. */
    CANCELLED_BY_EVENT,

    /** The supplied amount was not a valid (positive, finite) value. */
    INVALID_AMOUNT,

    /** The operation failed for an unspecified reason. */
    UNKNOWN_ERROR
}
