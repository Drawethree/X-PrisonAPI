package dev.drawethree.xprison.api.text;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Renders and sends MiniMessage-formatted text through X-Prison's text pipeline.
 *
 * <p>Addons should use this instead of depending on Adventure directly. X-Prison does not shade
 * Adventure: it uses the copy Paper bundles, and on servers that do not provide one (Spigot,
 * CraftBukkit) it falls back to rendering MiniMessage into legacy section codes. An addon that
 * imports {@code net.kyori} itself therefore works on Paper but fails to link on Spigot - and
 * nothing warns you at compile time, because Paper's API brings Adventure in transitively.
 *
 * <p>Everything here is {@link String} in, {@link String} out. Pass raw config text, with
 * MiniMessage tags and/or legacy {@code &} codes; the plugin decides how to render it for the
 * server it is running on. Do not pre-render text before passing it in - rendering twice is how
 * player-supplied values (names, nicknames) end up recolouring the message around them.
 *
 * <p>All methods are safe to call from any thread and never throw on malformed input.
 *
 * @since 1.9
 */
public interface XPrisonTextAPI {

    /**
     * Sends a message, resolving PlaceholderAPI placeholders first when available.
     *
     * @param target      recipient; players and the console are both accepted
     * @param miniMessage raw message text
     */
    void sendMessage(@NotNull CommandSender target, @Nullable String miniMessage);

    /**
     * Sends several messages as separate lines.
     *
     * @param target      recipient
     * @param miniMessage raw message lines
     */
    void sendMessage(@NotNull CommandSender target, @NotNull List<String> miniMessage);

    /**
     * Sends an action bar message.
     *
     * @param target      recipient
     * @param miniMessage raw message text
     */
    void sendActionBar(@NotNull Player target, @Nullable String miniMessage);

    /**
     * Sends a title and subtitle.
     *
     * @param target       recipient
     * @param title        raw title text
     * @param subtitle     raw subtitle text
     * @param fadeInTicks  fade-in duration in ticks
     * @param stayTicks    on-screen duration in ticks
     * @param fadeOutTicks fade-out duration in ticks
     */
    void sendTitle(@NotNull Player target, @Nullable String title, @Nullable String subtitle,
                   int fadeInTicks, int stayTicks, int fadeOutTicks);

    /**
     * Sets an item's display name from MiniMessage text.
     *
     * <p>Use this rather than {@code ItemMeta#displayName(Component)}: that overload names an
     * Adventure type in its signature, which is enough on its own to stop the calling class from
     * loading on a server without Adventure.
     *
     * <p>Item text renders in italics by default. Prefix the string with {@code <italic:false>} to
     * turn that off, as X-Prison's own configs do.
     *
     * @param meta        the meta to modify in place
     * @param miniMessage raw display name
     */
    void applyDisplayName(@NotNull ItemMeta meta, @Nullable String miniMessage);

    /**
     * Replaces an item's lore with the given MiniMessage lines.
     *
     * @param meta        the meta to modify in place
     * @param miniMessage raw lore lines
     */
    void applyLore(@NotNull ItemMeta meta, @NotNull List<String> miniMessage);

    /**
     * Escapes MiniMessage tags so they render as literal text.
     *
     * <p>Apply this to anything a player can type before putting it into a message - chat relayed
     * from another platform, nicknames, gang names, sign text. Without it a player can type
     * {@code <click:run_command:'/op them'>} into your message and have it rendered as a real
     * clickable tag.
     *
     * @param text untrusted text
     * @return the text with tag syntax neutralised
     */
    @NotNull
    String escapeTags(@Nullable String text);

    /**
     * Renders text to legacy section codes.
     *
     * <p>For APIs that take a coloured {@link String} rather than sending it themselves -
     * holograms, scoreboards, and values returned from a PlaceholderAPI expansion. Gradients are
     * downsampled per character; hover and click cannot be represented and are dropped.
     *
     * @param miniMessage raw text
     * @return the rendered text, never {@code null}
     */
    @NotNull
    String toLegacySection(@Nullable String miniMessage);

    /**
     * Strips all formatting, leaving the plain text a player reads.
     *
     * <p>Use this for length limits and name comparisons. Bukkit's {@code ChatColor.stripColor}
     * does nothing to MiniMessage tags, so a name written as
     * {@code <gradient:#FFD700:#FFAA00>Elite</gradient>} measures 5 characters here and 46 there.
     *
     * @param miniMessage raw text
     * @return the plain text, never {@code null}
     */
    @NotNull
    String stripTags(@Nullable String miniMessage);

    /**
     * Whether the active renderer supports the full MiniMessage feature set.
     *
     * <p>{@code true} on Paper-family servers. When {@code false}, hover and click tags are dropped
     * during rendering, so an addon offering a clickable link should provide a plain alternative.
     *
     * @return {@code true} when hover, click and fonts are available
     */
    boolean isRich();
}
