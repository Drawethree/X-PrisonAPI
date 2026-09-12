package dev.drawethree.xprison.api.diagnostics;

import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

/**
 * One permission node X-Prison checks, together with a description of what it gates and who
 * holds it before any permissions plugin says otherwise.
 *
 * <p>Entries are generated from the plugin's own permission constants, so a node cannot be added
 * to X-Prison without appearing here. X-Prison registers every non-prefix node with Bukkit under
 * its {@link #defaultValue() default} at startup, so an explicit grant or deny from a permissions
 * plugin always wins over it.
 *
 * @param node         the permission node, e.g. {@code "xprison.menu.other"}. When {@link #prefix()}
 *                     is {@code true} this is only the stem of the real node
 * @param description  what granting the node allows, in plain language
 * @param prefix       {@code true} when the node is a prefix that is completed at runtime - for
 *                     example a per-mine or per-currency suffix is appended before the check. Such
 *                     a node is never checked verbatim and is not registered with Bukkit
 * @param defaultValue who holds the node when no permissions plugin has set it: {@code TRUE} for
 *                     every player, {@code OP} for operators only, {@code FALSE} for nobody. Prefix
 *                     nodes report {@code OP}, which is Bukkit's own default for an undeclared node
 * @since 1.9
 */
public record PermissionEntry(@NotNull String node,
                              @NotNull String description,
                              boolean prefix,
                              @NotNull PermissionDefault defaultValue) {

    /**
     * Creates an entry that defaults to {@link PermissionDefault#OP}, the value Bukkit gives an
     * undeclared node. Kept for callers written against 1.9, which had no default.
     *
     * @param node        the permission node
     * @param description what granting the node allows
     * @param prefix      whether the node is a runtime-completed prefix
     * @since 1.10
     */
    public PermissionEntry(@NotNull String node, @NotNull String description, boolean prefix) {
        this(node, description, prefix, PermissionDefault.OP);
    }
}
