package dev.drawethree.xprison.api.diagnostics;

import org.jetbrains.annotations.NotNull;

/**
 * One problem found by {@link XPrisonDiagnosticsAPI#lintConfiguration()}.
 *
 * <p>A finding describes a configuration mistake in plain language. It is not tied to a line
 * number: the linter reads the loaded configuration, not the file text, so {@link #source()}
 * names the file or section the problem belongs to rather than a position inside it.
 *
 * @param severity how seriously to take it
 * @param source   where the problem lives, for grouping - typically a config file name such as
 *                 {@code "enchants.yml"} or a section such as {@code "Mine: quarry"}
 * @param message  a human-readable description, ready to show to a server owner
 * @since 1.9
 */
public record ConfigFinding(@NotNull FindingSeverity severity,
                            @NotNull String source,
                            @NotNull String message) {

    /**
     * Whether this finding is an {@link FindingSeverity#ERROR}.
     *
     * @return {@code true} if the affected feature is broken as configured
     */
    public boolean isError() {
        return this.severity == FindingSeverity.ERROR;
    }
}
