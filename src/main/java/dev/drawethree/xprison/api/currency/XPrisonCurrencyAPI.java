package dev.drawethree.xprison.api.currency;

import dev.drawethree.xprison.api.currency.enums.LostCause;
import dev.drawethree.xprison.api.currency.enums.ReceiveCause;
import dev.drawethree.xprison.api.currency.enums.TransactionStatus;
import dev.drawethree.xprison.api.currency.model.XPrisonCurrency;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface XPrisonCurrencyAPI {

    /**
     * Registers a new custom currency to the system.
     * If a currency with the same name (case-insensitive) already exists, it will be overwritten.
     *
     * @param currency The {@link XPrisonCurrency} implementation to register.
     */
    void registerCurrency(XPrisonCurrency currency);

    /**
     * Unregisters a currency from the system.
     * Player balances and related data for the currency may be removed or archived.
     *
     * @param currency The {@link XPrisonCurrency} implementation to unregister.
     */
    void unregisterCurrency(XPrisonCurrency currency);

    /**
     * Retrieves a currency by its name.
     *
     * @param name The name of the currency (case-insensitive).
     * @return The matching {@link XPrisonCurrency}, or null if not found.
     */
    XPrisonCurrency getCurrency(String name);

    /**
     * Returns all registered currencies.
     *
     * @return A collection of all {@link XPrisonCurrency} objects.
     */
    Collection<XPrisonCurrency> getAllCurrencies();

    /**
     * Gets the current amount of a specific currency for the specified player.
     *
     * @param player The player whose balance to retrieve.
     * @param currencyName The currency to check.
     * @return The amount of currency the player currently has.
     * @deprecated a {@code double} loses integer precision above ~9 quadrillion (2^53); for
     *             OP-scale balances use {@link #getBalanceExact(OfflinePlayer, String)} instead.
     */
    @Deprecated
    double getBalance(OfflinePlayer player, String currencyName);

    /**
     * Adds a specified amount of a specific currency to the player's balance.
     *
     * @param player The player to add currency to.
     * @param currencyName The currency to add.
     * @param amount The amount to add.
     * @param receiveCause The reason for receiving the currency.
     * @return true if added successfully, false otherwise.
     */
    boolean addBalance(OfflinePlayer player, String currencyName, double amount, ReceiveCause receiveCause);

    /**
     * Removes a specified amount of a specific currency from the player's balance.
     *
     * @param player The player to remove currency from.
     * @param currencyName The currency to remove.
     * @param amount The amount to remove.
     * @param lostCause The reason for removing the currency.
     * @return true if removed successfully, false otherwise.
     */
    boolean removeBalance(OfflinePlayer player, String currencyName, double amount, LostCause lostCause);

    /**
     * Sets the balance of a specific currency for the player.
     *
     * @param player The player whose balance to set.
     * @param currencyName The currency to set.
     * @param amount The amount to set.
     * @return true if set successfully, false otherwise.
     */
    boolean setBalance(OfflinePlayer player, String currencyName, double amount);

    /**
     * Checks if the player has at least the specified amount of the given currency.
     *
     * @param player The player to check.
     * @param currencyName The currency to check.
     * @param amount The minimum amount required.
     * @return true if player has enough, false otherwise.
     */
    boolean has(OfflinePlayer player, String currencyName, double amount);

    /**
     * Returns the top N players by balance for the given currency, ordered descending.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @return ordered map of UUID → balance
     * @deprecated the {@code Double} values lose precision above ~9 quadrillion (2^53); use
     *             {@link #getTopByBalanceExact(String, int)} for exact balances.
     */
    @Deprecated
    Map<UUID, Double> getTopByBalance(String currencyName, int limit);

    /**
     * Creates a new currency, persists it to {@code currencies.yml}, and registers it in memory.
     * <p>
     * If {@link XPrisonCurrency#getItemConfig()} returns non-{@code null}, the physical-item
     * section is written to the config as well.
     * <p>
     * Note: this differs from {@link #registerCurrency(XPrisonCurrency)}, which only updates the
     * in-memory registry without persisting to disk.
     *
     * @param currency the currency to create
     * @return {@code true} on success; {@code false} if a currency with the same name already exists
     */
    boolean createCurrency(XPrisonCurrency currency);

    /**
     * Updates an existing currency's configuration in {@code currencies.yml} and re-registers it
     * in memory with the new values.
     * <p>
     * If the updated currency's {@link XPrisonCurrency#getItemConfig()} returns {@code null},
     * the physical-item section is removed from the config.
     *
     * @param currency the currency carrying the updated values (identified by {@link XPrisonCurrency#getName()})
     * @return {@code true} on success; {@code false} if no currency with that name exists
     */
    boolean updateCurrency(XPrisonCurrency currency);

    /**
     * Removes a currency from {@code currencies.yml} and unregisters it from memory.
     * <p>
     * Currencies backed by an external {@link dev.drawethree.xprison.api.currency.model.XPrisonCurrencyHandler}
     * cannot be deleted through this method.
     *
     * @param name the currency name (case-insensitive)
     * @return {@code true} on success; {@code false} if not found or has an external handler
     */
    boolean deleteCurrency(String name);

    /**
     * Returns the top N players by balance for the given currency, with an optional offset for pagination.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @param offset       number of entries to skip (0 = start from top)
     * @return ordered map of UUID → balance
     * @deprecated the {@code Double} values lose precision above ~9 quadrillion (2^53); use
     *             {@link #getTopByBalanceExact(String, int, int)} for exact balances.
     */
    @Deprecated
    @NotNull
    Map<UUID, Double> getTopByBalance(String currencyName, int limit, int offset);

    /**
     * Transfers an amount of currency from one player to another atomically.
     * Fails if the source player does not have enough balance.
     *
     * @param from         the player sending the currency
     * @param to           the player receiving the currency
     * @param currencyName the currency name
     * @param amount       the amount to transfer (must be positive)
     * @return {@code true} if the transfer succeeded; {@code false} if the source had insufficient funds or currency not found
     */
    boolean transferBalance(OfflinePlayer from, OfflinePlayer to, String currencyName, double amount);

    // ------------------------------------------------------------------
    // Richer-return variants (status-aware). These mirror the boolean
    // methods above but report exactly why an operation succeeded/failed.
    // ------------------------------------------------------------------

    /**
     * Adds currency to a player and reports the outcome.
     *
     * @param player       the player to add currency to
     * @param currencyName the currency to add
     * @param amount       the amount to add (must be positive and finite)
     * @param receiveCause the reason for receiving the currency
     * @return the {@link TransactionStatus} describing the outcome
     */
    @NotNull
    TransactionStatus tryAddBalance(OfflinePlayer player, String currencyName, double amount, ReceiveCause receiveCause);

    /**
     * Removes currency from a player and reports the outcome.
     *
     * @param player       the player to remove currency from
     * @param currencyName the currency to remove
     * @param amount       the amount to remove (must be positive and finite)
     * @param lostCause    the reason for removing the currency
     * @return the {@link TransactionStatus} describing the outcome
     *         ({@link TransactionStatus#INSUFFICIENT_FUNDS} if the player lacks the balance)
     */
    @NotNull
    TransactionStatus tryRemoveBalance(OfflinePlayer player, String currencyName, double amount, LostCause lostCause);

    /**
     * Transfers currency between two players and reports the outcome.
     *
     * @param from         the player sending the currency
     * @param to           the player receiving the currency
     * @param currencyName the currency to transfer
     * @param amount       the amount to transfer (must be positive and finite)
     * @return the {@link TransactionStatus} describing the outcome
     */
    @NotNull
    TransactionStatus tryTransferBalance(OfflinePlayer from, OfflinePlayer to, String currencyName, double amount);

    /**
     * Adds the same amount of currency to many players in one call.
     * Useful for payouts and reward distribution without issuing N separate calls.
     *
     * @param players      the players to credit
     * @param currencyName the currency to add
     * @param amount       the amount to add to each player
     * @param receiveCause the reason for receiving the currency
     * @return a map of each player UUID to the {@link TransactionStatus} of their individual operation
     */
    @NotNull
    Map<UUID, TransactionStatus> addBalanceBulk(Collection<OfflinePlayer> players, String currencyName, double amount, ReceiveCause receiveCause);

    // ------------------------------------------------------------------
    // Async (non-blocking) read variants. Default implementations run the
    // corresponding synchronous read on the common pool so callers never
    // block the server thread on a database query.
    // ------------------------------------------------------------------

    /**
     * Asynchronous variant of {@link #getBalance(OfflinePlayer, String)}.
     *
     * @param player       the player whose balance to retrieve
     * @param currencyName the currency to check
     * @return a future completing with the player's balance
     */
    @NotNull
    default CompletableFuture<Double> getBalanceAsync(OfflinePlayer player, String currencyName) {
        return CompletableFuture.supplyAsync(() -> getBalance(player, currencyName));
    }

    /**
     * Asynchronous variant of {@link #getTopByBalance(String, int)}.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @return a future completing with the ordered map of UUID → balance
     * @deprecated the {@code Double} values lose precision above ~9 quadrillion (2^53); use
     *             {@link #getTopByBalanceExactAsync(String, int)} for exact balances.
     */
    @Deprecated
    @NotNull
    default CompletableFuture<Map<UUID, Double>> getTopByBalanceAsync(String currencyName, int limit) {
        return CompletableFuture.supplyAsync(() -> getTopByBalance(currencyName, limit));
    }

    /**
     * Asynchronous variant of {@link #getTopByBalance(String, int, int)}.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @param offset       number of entries to skip (0 = start from top)
     * @return a future completing with the ordered map of UUID → balance
     * @deprecated the {@code Double} values lose precision above ~9 quadrillion (2^53); use
     *             {@link #getTopByBalanceExactAsync(String, int, int)} for exact balances.
     */
    @Deprecated
    @NotNull
    default CompletableFuture<Map<UUID, Double>> getTopByBalanceAsync(String currencyName, int limit, int offset) {
        return CompletableFuture.supplyAsync(() -> getTopByBalance(currencyName, limit, offset));
    }

    // ------------------------------------------------------------------
    // Exact (BigDecimal) variants. Currency balances can exceed a double's
    // ~9 quadrillion (2^53) integer-precision limit on OP-prison servers, so
    // these preserve every unit. Each default delegates to the legacy double
    // method; the implementation overrides them with the exact path.
    // ------------------------------------------------------------------

    /**
     * Exact-precision variant of {@link #getBalance(OfflinePlayer, String)}.
     *
     * @param player       the player whose balance to retrieve
     * @param currencyName the currency to check
     * @return the player's exact balance, never {@code null}
     */
    @NotNull
    default BigDecimal getBalanceExact(OfflinePlayer player, String currencyName) {
        return BigDecimal.valueOf(getBalance(player, currencyName));
    }

    /**
     * Exact-precision variant of {@link #addBalance(OfflinePlayer, String, double, ReceiveCause)}.
     *
     * @param player       the player to add currency to
     * @param currencyName the currency to add
     * @param amount       the amount to add
     * @param receiveCause the reason for receiving the currency
     * @return {@code true} if added successfully, {@code false} otherwise
     */
    default boolean addBalance(OfflinePlayer player, String currencyName, BigDecimal amount, ReceiveCause receiveCause) {
        return addBalance(player, currencyName, amount.doubleValue(), receiveCause);
    }

    /**
     * Exact-precision variant of {@link #removeBalance(OfflinePlayer, String, double, LostCause)}.
     *
     * @param player       the player to remove currency from
     * @param currencyName the currency to remove
     * @param amount       the amount to remove
     * @param lostCause    the reason for removing the currency
     * @return {@code true} if removed successfully, {@code false} otherwise
     */
    default boolean removeBalance(OfflinePlayer player, String currencyName, BigDecimal amount, LostCause lostCause) {
        return removeBalance(player, currencyName, amount.doubleValue(), lostCause);
    }

    /**
     * Exact-precision variant of {@link #setBalance(OfflinePlayer, String, double)}.
     *
     * @param player       the player whose balance to set
     * @param currencyName the currency to set
     * @param amount       the amount to set
     * @return {@code true} if set successfully, {@code false} otherwise
     */
    default boolean setBalance(OfflinePlayer player, String currencyName, BigDecimal amount) {
        return setBalance(player, currencyName, amount.doubleValue());
    }

    /**
     * Exact-precision variant of {@link #has(OfflinePlayer, String, double)}.
     *
     * @param player       the player to check
     * @param currencyName the currency to check
     * @param amount       the minimum amount required
     * @return {@code true} if the player has enough, {@code false} otherwise
     */
    default boolean has(OfflinePlayer player, String currencyName, BigDecimal amount) {
        return has(player, currencyName, amount.doubleValue());
    }

    /**
     * Exact-precision variant of
     * {@link #transferBalance(OfflinePlayer, OfflinePlayer, String, double)}.
     *
     * @param from         the player sending the currency
     * @param to           the player receiving the currency
     * @param currencyName the currency name
     * @param amount       the amount to transfer (must be positive)
     * @return {@code true} if the transfer succeeded, {@code false} otherwise
     */
    default boolean transferBalance(OfflinePlayer from, OfflinePlayer to, String currencyName, BigDecimal amount) {
        return transferBalance(from, to, currencyName, amount.doubleValue());
    }

    /**
     * Exact-precision variant of
     * {@link #tryAddBalance(OfflinePlayer, String, double, ReceiveCause)}.
     *
     * @param player       the player to add currency to
     * @param currencyName the currency to add
     * @param amount       the amount to add (must be positive)
     * @param receiveCause the reason for receiving the currency
     * @return the {@link TransactionStatus} describing the outcome
     */
    @NotNull
    default TransactionStatus tryAddBalance(OfflinePlayer player, String currencyName, BigDecimal amount, ReceiveCause receiveCause) {
        return tryAddBalance(player, currencyName, amount.doubleValue(), receiveCause);
    }

    /**
     * Exact-precision variant of
     * {@link #tryRemoveBalance(OfflinePlayer, String, double, LostCause)}.
     *
     * @param player       the player to remove currency from
     * @param currencyName the currency to remove
     * @param amount       the amount to remove (must be positive)
     * @param lostCause    the reason for removing the currency
     * @return the {@link TransactionStatus} describing the outcome
     */
    @NotNull
    default TransactionStatus tryRemoveBalance(OfflinePlayer player, String currencyName, BigDecimal amount, LostCause lostCause) {
        return tryRemoveBalance(player, currencyName, amount.doubleValue(), lostCause);
    }

    /**
     * Exact-precision variant of
     * {@link #tryTransferBalance(OfflinePlayer, OfflinePlayer, String, double)}.
     *
     * @param from         the player sending the currency
     * @param to           the player receiving the currency
     * @param currencyName the currency to transfer
     * @param amount       the amount to transfer (must be positive)
     * @return the {@link TransactionStatus} describing the outcome
     */
    @NotNull
    default TransactionStatus tryTransferBalance(OfflinePlayer from, OfflinePlayer to, String currencyName, BigDecimal amount) {
        return tryTransferBalance(from, to, currencyName, amount.doubleValue());
    }

    /**
     * Exact-precision variant of {@link #getTopByBalance(String, int)}.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @return ordered map of UUID → exact balance
     */
    @NotNull
    default Map<UUID, BigDecimal> getTopByBalanceExact(String currencyName, int limit) {
        return toBigDecimalMap(getTopByBalance(currencyName, limit));
    }

    /**
     * Exact-precision variant of {@link #getTopByBalance(String, int, int)}.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @param offset       number of entries to skip (0 = start from top)
     * @return ordered map of UUID → exact balance
     */
    @NotNull
    default Map<UUID, BigDecimal> getTopByBalanceExact(String currencyName, int limit, int offset) {
        return toBigDecimalMap(getTopByBalance(currencyName, limit, offset));
    }

    /**
     * Asynchronous variant of {@link #getBalanceExact(OfflinePlayer, String)}.
     *
     * @param player       the player whose balance to retrieve
     * @param currencyName the currency to check
     * @return a future completing with the player's exact balance
     */
    @NotNull
    default CompletableFuture<BigDecimal> getBalanceExactAsync(OfflinePlayer player, String currencyName) {
        return CompletableFuture.supplyAsync(() -> getBalanceExact(player, currencyName));
    }

    /**
     * Asynchronous variant of {@link #getTopByBalanceExact(String, int)}.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @return a future completing with the ordered map of UUID → exact balance
     */
    @NotNull
    default CompletableFuture<Map<UUID, BigDecimal>> getTopByBalanceExactAsync(String currencyName, int limit) {
        return CompletableFuture.supplyAsync(() -> getTopByBalanceExact(currencyName, limit));
    }

    /**
     * Asynchronous variant of {@link #getTopByBalanceExact(String, int, int)}.
     *
     * @param currencyName the currency name
     * @param limit        maximum number of entries to return
     * @param offset       number of entries to skip (0 = start from top)
     * @return a future completing with the ordered map of UUID → exact balance
     */
    @NotNull
    default CompletableFuture<Map<UUID, BigDecimal>> getTopByBalanceExactAsync(String currencyName, int limit, int offset) {
        return CompletableFuture.supplyAsync(() -> getTopByBalanceExact(currencyName, limit, offset));
    }

    /** Widens a {@code UUID → Double} leaderboard map to {@code UUID → BigDecimal}, preserving order. */
    static Map<UUID, BigDecimal> toBigDecimalMap(Map<UUID, Double> source) {
        Map<UUID, BigDecimal> out = new LinkedHashMap<>();
        for (Map.Entry<UUID, Double> entry : source.entrySet()) {
            out.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }
        return out;
    }
}
