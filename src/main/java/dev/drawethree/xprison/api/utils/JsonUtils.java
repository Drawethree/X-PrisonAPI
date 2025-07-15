package dev.drawethree.xprison.api.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class JsonUtils {

    public static String getRequiredString(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Missing or null key in enchantment config: \"" + key + "\"");
        }
        return obj.get(key).getAsString();
    }

    public static int getRequiredInt(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Missing or null key in enchantment config: \"" + key + "\"");
        }
        return obj.get(key).getAsInt();
    }

    public static long getRequiredLong(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Missing or null key in enchantment config: \"" + key + "\"");
        }
        return obj.get(key).getAsLong();
    }

    public static boolean getRequiredBoolean(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Missing or null key in enchantment config: \"" + key + "\"");
        }
        return obj.get(key).getAsBoolean();
    }

    public static JsonObject getRequiredObject(JsonObject parent, String key) {
        if (!parent.has(key)) {
            throw new IllegalArgumentException("Missing required object key: \"" + key + "\"");
        }

        JsonElement element = parent.get(key);
        if (element == null || element.isJsonNull()) {
            throw new IllegalArgumentException("Key \"" + key + "\" is null in JSON object.");
        }

        if (!element.isJsonObject()) {
            throw new IllegalArgumentException("Key \"" + key + "\" is not a JsonObject.");
        }

        return element.getAsJsonObject();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRequired(JsonObject obj, String key, Class<T> clazz) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Missing or null key: \"" + key + "\"");
        }
        return new Gson().fromJson(obj.get(key), clazz);
    }

    public static List<String> getRequiredStringList(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            throw new IllegalArgumentException("Missing or null key: \"" + key + "\"");
        }
        return new Gson().fromJson(obj.get(key), new TypeToken<List<String>>() {}.getType());
    }

    public static String getOptionalString(JsonObject obj, String key, String defaultValue) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return defaultValue;
        }

        JsonElement element = obj.get(key);

        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            return defaultValue;
        }

        return element.getAsString();
    }

    public static int getOptionalInt(JsonObject obj, String key, int defaultValue) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return defaultValue;
        }

        JsonElement element = obj.get(key);

        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            return defaultValue;
        }

        return element.getAsInt();
    }


}