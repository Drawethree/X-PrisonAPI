package dev.drawethree.xprison.api.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests for the optional getters, which previously rejected any value that was not a
 * string — so a plain JSON number or boolean silently fell back to the default.
 */
class JsonUtilsOptionalTest {

	private static JsonObject json(String raw) {
		return JsonParser.parseString(raw).getAsJsonObject();
	}

	@Test
	@DisplayName("a JSON number is read, not defaulted")
	void readsRealNumbers() {
		assertEquals(250, JsonUtils.getOptionalInt(json("{\"v\":250}"), "v", 0));
		assertEquals(2.5D, JsonUtils.getOptionalDouble(json("{\"v\":2.5}"), "v", 0.0D));
	}

	@Test
	@DisplayName("a JSON boolean is read, not defaulted")
	void readsRealBooleans() {
		assertTrue(JsonUtils.getOptionalBoolean(json("{\"v\":true}"), "v", false));
		assertFalse(JsonUtils.getOptionalBoolean(json("{\"v\":false}"), "v", true));
	}

	@Test
	@DisplayName("quoted numbers and booleans are tolerated")
	void tolerantOfQuotedValues() {
		assertEquals(7, JsonUtils.getOptionalInt(json("{\"v\":\"7\"}"), "v", 0));
		assertEquals(1.5D, JsonUtils.getOptionalDouble(json("{\"v\":\"1.5\"}"), "v", 0.0D));
		assertTrue(JsonUtils.getOptionalBoolean(json("{\"v\":\"TRUE\"}"), "v", false));
		assertFalse(JsonUtils.getOptionalBoolean(json("{\"v\":\"false\"}"), "v", true));
	}

	@Test
	@DisplayName("absent, null and unparseable values fall back to the default")
	void fallsBackWhenUnusable() {
		assertEquals(3, JsonUtils.getOptionalInt(json("{}"), "v", 3));
		assertEquals(3, JsonUtils.getOptionalInt(json("{\"v\":null}"), "v", 3));
		assertEquals(3, JsonUtils.getOptionalInt(json("{\"v\":\"abc\"}"), "v", 3));
		assertEquals(3, JsonUtils.getOptionalInt(json("{\"v\":{}}"), "v", 3));
		assertTrue(JsonUtils.getOptionalBoolean(json("{\"v\":\"maybe\"}"), "v", true));
		assertEquals("d", JsonUtils.getOptionalString(json("{\"v\":null}"), "v", "d"));
	}
}
