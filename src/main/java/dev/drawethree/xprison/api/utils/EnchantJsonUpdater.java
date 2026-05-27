package dev.drawethree.xprison.api.utils;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Merges missing keys from a JAR-bundled default JSON into an on-disk config file.
 * <p>
 * Call this in your addon's {@code onEnable} after ensuring the file exists so that
 * new configuration keys added in a release are automatically propagated to existing
 * installations. Existing values are never overwritten.
 */
public final class EnchantJsonUpdater {

    private static final Logger LOGGER = Logger.getLogger(EnchantJsonUpdater.class.getName());

    private EnchantJsonUpdater() {}

    /**
     * Adds any keys present in {@code defaultStream} that are absent from {@code file},
     * then saves the file only if something changed. JSON objects are merged recursively;
     * primitive values and arrays are left untouched if the key already exists.
     *
     * @param file          on-disk JSON config to update
     * @param defaultStream JAR-bundled default, e.g. from {@code getClass().getResourceAsStream()}
     */
    public static void update(File file, InputStream defaultStream) {
        if (!file.exists() || defaultStream == null) return;

        JsonObject defaults;
        try (InputStreamReader reader = new InputStreamReader(defaultStream, StandardCharsets.UTF_8)) {
            defaults = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.warning("EnchantJsonUpdater: failed to read JAR default for " + file.getName() + ": " + e.getMessage());
            return;
        }

        JsonObject current;
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            current = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.warning("EnchantJsonUpdater: failed to read " + file.getName() + ": " + e.getMessage());
            return;
        }

        List<String> added = new ArrayList<>();
        if (!mergeObjects(current, defaults, added, "")) return;

        try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(current, writer);
            LOGGER.info("EnchantJsonUpdater: '" + file.getName() + "' updated — added " + added.size() + " key(s): " + added);
        } catch (IOException e) {
            LOGGER.warning("EnchantJsonUpdater: failed to save '" + file.getName() + "': " + e.getMessage());
        }
    }

    private static boolean mergeObjects(JsonObject target, JsonObject defaults,
                                        List<String> added, String path) {
        boolean changed = false;
        for (Map.Entry<String, JsonElement> entry : defaults.entrySet()) {
            String key = entry.getKey();
            JsonElement defaultVal = entry.getValue();
            String fullPath = path.isEmpty() ? key : path + "." + key;

            if (!target.has(key)) {
                target.add(key, defaultVal.deepCopy());
                added.add(fullPath);
                changed = true;
            } else if (defaultVal.isJsonObject() && target.get(key).isJsonObject()) {
                changed |= mergeObjects(target.get(key).getAsJsonObject(),
                        defaultVal.getAsJsonObject(), added, fullPath);
            }
        }
        return changed;
    }
}
