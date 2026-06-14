package dev.drawethree.xprison.api.shared;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Small helper for paginating already-ordered leaderboard maps.
 * <p>
 * Used by the {@code default} {@code offset} overloads of the various {@code getTopBy*} methods so a
 * single, order-preserving slicing implementation is shared across modules. Callers that need true
 * database-level offset paging (rather than over-fetch-and-slice) should use a module method that
 * accepts an {@code offset} directly where one is provided.
 */
public final class Pagination {

	private Pagination() {
	}

	/**
	 * Returns an order-preserving slice of {@code ordered}, skipping {@code offset} entries and
	 * keeping at most {@code limit}.
	 *
	 * @param ordered the source map, assumed to already be in the desired order (e.g. a {@link LinkedHashMap})
	 * @param limit   the maximum number of entries to keep (negative is treated as 0)
	 * @param offset  the number of leading entries to skip (negative is treated as 0)
	 * @param <K>     key type
	 * @param <V>     value type
	 * @return a new {@link LinkedHashMap} containing the requested slice
	 */
	public static <K, V> Map<K, V> slice(Map<K, V> ordered, int limit, int offset) {
		Map<K, V> out = new LinkedHashMap<>();
		if (ordered == null || limit <= 0) {
			return out;
		}
		int skip = Math.max(offset, 0);
		int seen = 0;
		for (Map.Entry<K, V> entry : ordered.entrySet()) {
			if (seen++ < skip) {
				continue;
			}
			if (out.size() >= limit) {
				break;
			}
			out.put(entry.getKey(), entry.getValue());
		}
		return out;
	}
}
