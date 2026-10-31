package dev.drawethree.xprison.api.language;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A config file an addon loaded itself, readable in the language of whoever is going to read the
 * text. Created with {@link XPrisonLanguageAPI#localize(String, String, YamlConfiguration, java.util.function.Function)}.
 *
 * <p>Every view is a separate, read-only copy of the config the addon passed in. A line is shown
 * translated when:
 * <ol>
 *   <li>the server owner translated it in
 *       {@code plugins/X-Prison/lang/addons/<addon>/<language>/<file>} - always, even where the owner
 *       edited the English text; or</li>
 *   <li>the addon ships a translation in its jar at {@code lang/<language>/<file>} and the line in
 *       the owner's config is still the addon's bundled English default.</li>
 * </ol>
 * Otherwise the line reads exactly as in the owner's config. Non-text settings are never changed.
 *
 * <p>Use {@link #get(CommandSender)} for anything a player reads - chat, titles, action bars, menu
 * titles and items, items you create for that player. Use {@link #get()} for console output and for
 * text everyone shares, such as holograms. Read numbers and other settings from either.
 *
 * <p>Views follow the server: when the owner changes {@code language:} or the installed languages
 * and runs {@code /xprison reload}, they are rebuilt on the next read. If the addon changes or
 * reloads its config, call {@code localize} again and replace this instance.
 *
 * <p>Reads are thread-safe.
 *
 * @since 1.10
 */
public interface LocalizedConfig {

    /**
     * Gets the config in the server language ({@code language:} in X-Prison's {@code config.yml}).
     *
     * @return the server-language view; never modify it
     */
    @NotNull
    YamlConfiguration get();

    /**
     * Gets the config in the language of whoever reads the text.
     *
     * @param viewer the player who reads the text; the console, a non-player sender or
     *               {@code null} reads the server language
     * @return the view in the viewer's language; never modify it
     */
    @NotNull
    YamlConfiguration get(@Nullable CommandSender viewer);

    /**
     * Gets the config in one language.
     *
     * @param languageCode a language code such as {@code de}; an unknown or disabled code reads the
     *                     server language
     * @return the view in that language; never modify it
     */
    @NotNull
    YamlConfiguration get(@NotNull String languageCode);
}
