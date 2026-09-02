package dev.drawethree.xprison.api.diagnostics;

/**
 * How seriously a {@link ConfigFinding} should be taken.
 *
 * @since 1.9
 */
public enum FindingSeverity {

    /**
     * The configuration is broken: the affected feature will not work as written. A menu that
     * cannot open, an enchant priced in a currency that does not exist, a mine whose block
     * percentages do not add up.
     */
    ERROR,

    /**
     * The configuration is usable but suspicious, and is likely not what the author meant.
     */
    WARNING
}
