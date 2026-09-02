package dev.drawethree.xprison.api.diagnostics;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Read-only access to X-Prison's self-checks: the configuration linter, the warnings collected
 * during startup, and the catalog of permission nodes.
 *
 * <p>This is the data behind {@code /xprison lint}, {@code /xprison health} and
 * {@code /xprison perms}. It exists so a management tool can present the same information without
 * scraping command output.
 *
 * <p>Nothing here mutates plugin state.
 *
 * @since 1.9
 */
public interface XPrisonDiagnosticsAPI {

    /**
     * Runs the configuration linter and returns everything it found.
     *
     * <p>The linter inspects the currently loaded configuration - enchant GUI slots across all four
     * enchant menus, enchant prices against the registered currencies, {@code supported-pickaxes},
     * and each mine's block composition and teleport point.
     *
     * <p><strong>Must be called from the server main thread.</strong> It reads live mine and
     * module state, which is not safe to touch from an async task.
     *
     * @return every finding, in the linter's own order; empty when the configuration is consistent
     * @throws IllegalStateException if called from a thread other than the server main thread
     */
    @NotNull
    List<ConfigFinding> lintConfiguration();

    /**
     * Returns the warnings X-Prison collected while enabling.
     *
     * <p>These are the same lines the startup digest summarises, kept so they can be read long
     * after the console has scrolled past them. The list is fixed once startup completes and does
     * not change until the next restart.
     *
     * @return the startup warnings in the order they were recorded; empty if there were none
     */
    @NotNull
    List<String> getStartupWarnings();

    /**
     * Returns every permission node X-Prison checks, grouped by the area it belongs to.
     *
     * <p>Keys are group names such as {@code "Mines"} or {@code "Enchants"}; each list is sorted by
     * node. The map is generated from the plugin's permission constants, so it is always complete
     * for the running version.
     *
     * @return permission entries grouped by area, never empty
     */
    @NotNull
    Map<String, List<PermissionEntry>> getPermissionCatalog();
}
