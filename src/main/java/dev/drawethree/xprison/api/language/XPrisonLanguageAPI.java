package dev.drawethree.xprison.api.language;

import com.google.gson.JsonObject;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * The language each player reads X-Prison in.
 *
 * <p>The server owner sets a default with {@code language:} in {@code config.yml}. Every player
 * starts on that default and may pick their own with {@code /xprison language}; the choice is stored in
 * the database. A player who picks "Server default" in that menu - or who never opens it - keeps
 * following {@code config.yml}, including when the owner changes it later. A player who picks a
 * concrete language keeps it.
 *
 * <p>Languages are identified by their code as it appears in {@code config.yml}: {@code en},
 * {@code de}, {@code es}, {@code fr}, {@code pt_br}, plus any code the server owner added. Codes are
 * lower case with an underscore;
 * {@link #setLanguage(UUID, String)} also accepts {@code pt-BR}.
 *
 * <p>This only covers X-Prison's own text. Console output, holograms and admin tools stay in the
 * server language. Addons keep their own messages unless they read {@link #getLanguage(UUID)}
 * themselves.
 *
 * <h2>Threading</h2>
 * Reads for an <em>online</em> player are served from memory and are safe anywhere. Reads for an
 * <em>offline</em> player hit the database and block, so run them off the main thread. Writes
 * never block: they update the cache when the player is online and persist asynchronously.
 *
 * @since 1.10
 */
public interface XPrisonLanguageAPI {

    /**
     * Gets the server's default language: {@code language:} in {@code config.yml}, or {@code en}
     * when that value is not an installed language.
     *
     * @return the code of the server language
     */
    @NotNull
    String getServerLanguage();

    /**
     * Gets every language a player can pick, English first: the bundled languages and any the owner
     * added in {@code plugins/X-Prison/lang/languages.yml}, limited to {@code enabled-languages} in
     * {@code config.yml} when that list is set. The server language is always included.
     *
     * @return an immutable, ordered map from language code to its display name, e.g.
     *         {@code de -> Deutsch}
     */
    @NotNull
    Map<String, String> getAvailableLanguages();

    /**
     * Gets the language this player currently reads X-Prison in: their own pick, or the server
     * language when they follow the server default.
     *
     * <p>Works for offline players; see the threading note on the interface.
     *
     * @param playerUuid the player
     * @return the code of the player's effective language
     */
    @NotNull
    String getLanguage(@NotNull UUID playerUuid);

    /**
     * Gets the language this player picked themselves.
     *
     * <p>Works for offline players; see the threading note on the interface.
     *
     * @param playerUuid the player
     * @return the code they picked, or empty when they follow the server default
     */
    @NotNull
    Optional<String> getChosenLanguage(@NotNull UUID playerUuid);

    /**
     * Sets the language this player reads X-Prison in.
     *
     * <p>Online players see the change immediately: their open menu is closed and their pickaxe
     * lore is redrawn. A {@link dev.drawethree.xprison.api.language.events.PlayerLanguageChangeEvent}
     * fires on the main thread when the choice actually changes. Works for offline players: the
     * value is persisted and applies the next time they log in.
     *
     * @param playerUuid   the player
     * @param languageCode the language to pin, or {@code null} to follow the server default
     * @return {@code false} when {@code languageCode} is not an installed language, in which case
     *         nothing changes; {@code true} otherwise
     */
    boolean setLanguage(@NotNull UUID playerUuid, @Nullable String languageCode);

    /**
     * Makes a config file an addon loaded itself readable in each player's language.
     *
     * <p>Load and merge your config the way you always do, then pass it here and read player-facing
     * text through the returned {@link LocalizedConfig}:
     *
     * <pre>{@code
     * YamlConfiguration raw = YamlConfiguration.loadConfiguration(file);
     * LocalizedConfig messages = XPrisonAPI.getInstance().getLanguageApi()
     *         .localize("Shop", "shop-messages.yml", raw, AddonResources.of(MyAddon.class));
     * String text = messages.get(player).getString("messages.bought");
     * }</pre>
     *
     * <p>Ship translations in your jar as {@code lang/<language>/<fileName>} containing only the
     * translated lines, under the same keys. The English default your translations were made for
     * is read from {@code <fileName>} in your jar. Server owners can add or override any line in
     * {@code plugins/X-Prison/lang/addons/<addonName>/<language>/<fileName>}.
     *
     * @param addonName        your addon's name, used for the owner override folder
     * @param fileName         the config's path relative to your data folder, e.g.
     *                         {@code messages.yml} or {@code shops/blocks.yml}
     * @param config           the config as you loaded it; it is copied and never changed
     * @param bundledResources reads a resource from your jar by path, returning {@code null} when it
     *                         does not exist; use {@link AddonResources#of(Class)}
     * @return the per-language views of {@code config}
     */
    @NotNull
    LocalizedConfig localize(@NotNull String addonName, @NotNull String fileName, @NotNull YamlConfiguration config,
                             @NotNull Function<String, InputStream> bundledResources);

    /**
     * Same as {@link #localize(String, String, YamlConfiguration, Function)} for a JSON config, such
     * as an enchant file. Translations are YAML files named after the JSON file:
     * {@code lang/<language>/<name>.yml} in your jar for {@code <name>.json}, and
     * {@code plugins/X-Prison/lang/addons/<addonName>/<language>/<name>.yml} for the owner.
     *
     * <p>Enchants extending {@link dev.drawethree.xprison.api.enchants.model.XPrisonEnchantmentBase}
     * get this automatically for their name, menu name, menu description and proc message.
     *
     * @param addonName        your addon's name, used for the owner override folder
     * @param fileName         the JSON file's name relative to your data folder, e.g. {@code petfinder.json}
     * @param config           the JSON as you loaded it; it is copied and never changed
     * @param bundledResources reads a resource from your jar by path, returning {@code null} when it
     *                         does not exist; use {@link AddonResources#of(Class)}. The English
     *                         default is read from {@code fileName}
     * @return the per-language views of {@code config}
     */
    @NotNull
    LocalizedJson localizeJson(@NotNull String addonName, @NotNull String fileName, @NotNull JsonObject config,
                               @NotNull Function<String, InputStream> bundledResources);
}
