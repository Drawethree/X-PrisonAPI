package dev.drawethree.xprison.api.diagnostics;

import org.jetbrains.annotations.NotNull;

/**
 * One permission node X-Prison checks, together with a description of what it gates.
 *
 * <p>Entries are generated from the plugin's own permission constants, so a node cannot be added
 * to X-Prison without appearing here.
 *
 * @param node        the permission node, e.g. {@code "xprison.menu.other"}. When {@link #prefix()}
 *                    is {@code true} this is only the stem of the real node
 * @param description what granting the node allows, in plain language
 * @param prefix      {@code true} when the node is a prefix that is completed at runtime - for
 *                    example a per-mine or per-currency suffix is appended before the check. Such
 *                    a node is never checked verbatim
 * @since 1.9
 */
public record PermissionEntry(@NotNull String node,
                              @NotNull String description,
                              boolean prefix) {
}
