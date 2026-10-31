package dev.drawethree.xprison.api.language;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * A JSON config an addon loaded itself (typically an enchant file), readable per language.
 * Created with {@link XPrisonLanguageAPI#localizeJson(String, String, JsonObject, java.util.function.Function)}.
 *
 * <p>Follows the same rules as {@link LocalizedConfig}: a line the owner translated in
 * {@code plugins/X-Prison/lang/addons/<addon>/<language>/<file>.yml} always wins, a translation
 * shipped in the addon jar at {@code lang/<language>/<file>.yml} applies while the owner's line is
 * still the bundled English default, and everything else reads as in the owner's file. Only text
 * values (strings and lists of strings) are ever replaced.
 *
 * @since 1.10
 */
public interface LocalizedJson {

    /**
     * Gets the config in the server language.
     *
     * @return a copy in the server language; changes to it are not kept
     */
    @NotNull
    JsonObject get();

    /**
     * Gets the config in one language.
     *
     * @param languageCode a language code such as {@code de}; an unknown or disabled code reads the
     *                     server language
     * @return a copy in that language; changes to it are not kept
     */
    @NotNull
    JsonObject get(@NotNull String languageCode);
}
