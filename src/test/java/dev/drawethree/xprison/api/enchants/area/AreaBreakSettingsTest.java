package dev.drawethree.xprison.api.enchants.area;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AreaBreakSettingsTest {

	private static JsonObject json(String raw) {
		return com.google.gson.JsonParser.parseString(raw).getAsJsonObject();
	}

	@Nested
	@DisplayName("event strategy resolution")
	class Strategy {

		@Test
		@DisplayName("explicit eventStrategy wins over everything")
		void explicitStrategyWins() {
			JsonObject config = json("{\"eventStrategy\":\"AGGREGATE\",\"useEvents\":true}");
			assertEquals(BreakEventStrategy.AGGREGATE,
					AreaBreakSettings.fromJson(config, BreakEventStrategy.NONE).eventStrategy());
		}

		@Test
		@DisplayName("eventStrategy is case-insensitive")
		void caseInsensitive() {
			JsonObject config = json("{\"eventStrategy\":\"per_block\"}");
			assertEquals(BreakEventStrategy.PER_BLOCK,
					AreaBreakSettings.fromJson(config, BreakEventStrategy.NONE).eventStrategy());
		}

		@Test
		@DisplayName("an unrecognised eventStrategy falls back rather than throwing")
		void unknownFallsBack() {
			JsonObject config = json("{\"eventStrategy\":\"nonsense\"}");
			assertEquals(BreakEventStrategy.AGGREGATE,
					AreaBreakSettings.fromJson(config, BreakEventStrategy.AGGREGATE).eventStrategy());
		}

		@Test
		@DisplayName("legacy useEvents:true maps to PER_BLOCK")
		void legacyTrueIsPerBlock() {
			JsonObject config = json("{\"useEvents\":true}");
			assertEquals(BreakEventStrategy.PER_BLOCK,
					AreaBreakSettings.fromJson(config, BreakEventStrategy.NONE).eventStrategy());
		}

		@Test
		@DisplayName("legacy useEvents:false maps to AGGREGATE")
		void legacyFalseIsAggregate() {
			JsonObject config = json("{\"useEvents\":false}");
			assertEquals(BreakEventStrategy.AGGREGATE,
					AreaBreakSettings.fromJson(config, BreakEventStrategy.NONE).eventStrategy());
		}

		@Test
		@DisplayName("with neither key the supplied default applies")
		void defaultApplies() {
			assertEquals(BreakEventStrategy.NONE,
					AreaBreakSettings.fromJson(json("{}"), BreakEventStrategy.NONE).eventStrategy());
		}
	}

	@Nested
	@DisplayName("shared fields")
	class Fields {

		@Test
		@DisplayName("defaults are applied for absent keys")
		void defaults() {
			AreaBreakSettings settings = AreaBreakSettings.fromJson(json("{}"), BreakEventStrategy.PER_BLOCK);
			assertFalse(settings.countBlocksBroken());
			assertEquals("money", settings.currencyToGive());
			assertEquals("", settings.message());
			assertEquals(0, settings.maxBlocks(), "0 means no cap");
		}

		@Test
		@DisplayName("values are read from the config")
		void readsValues() {
			AreaBreakSettings settings = AreaBreakSettings.fromJson(
					json("{\"countBlocksBroken\":true,\"currencyToGive\":\"tokens\",\"message\":\"hi\",\"maxBlocks\":250}"),
					BreakEventStrategy.PER_BLOCK);
			assertTrue(settings.countBlocksBroken());
			assertEquals("tokens", settings.currencyToGive());
			assertEquals("hi", settings.message());
			assertEquals(250, settings.maxBlocks());
		}

		@Test
		@DisplayName("a negative maxBlocks is clamped to no-cap")
		void negativeCapClamped() {
			assertEquals(0, AreaBreakSettings.fromJson(json("{\"maxBlocks\":-5}"), BreakEventStrategy.PER_BLOCK).maxBlocks());
		}

		@Test
		@DisplayName("removeBlocks defaults to true so existing enchants keep clearing")
		void removeBlocksDefaultsTrue() {
			assertTrue(AreaBreakSettings.fromJson(json("{}"), BreakEventStrategy.PER_BLOCK).removeBlocks());
		}

		@Test
		@DisplayName("removeBlocks:false selects reward-only mode")
		void removeBlocksRewardOnly() {
			assertFalse(AreaBreakSettings.fromJson(json("{\"removeBlocks\":false}"), BreakEventStrategy.PER_BLOCK).removeBlocks());
		}

		@Test
		@DisplayName("reward-only forces NONE, overriding any configured strategy")
		void rewardOnlyForcesNoEvents() {
			// Explicitly configured strategies must not survive: nothing broke, so nothing is announced.
			assertEquals(BreakEventStrategy.NONE,
					AreaBreakSettings.fromJson(json("{\"removeBlocks\":false,\"eventStrategy\":\"PER_BLOCK\"}"),
							BreakEventStrategy.PER_BLOCK).eventStrategy());
			assertEquals(BreakEventStrategy.NONE,
					AreaBreakSettings.fromJson(json("{\"removeBlocks\":false,\"useEvents\":true}"),
							BreakEventStrategy.PER_BLOCK).eventStrategy());
			// ...and the invariant holds for direct construction too, not just config parsing.
			assertEquals(BreakEventStrategy.NONE,
					new AreaBreakSettings(true, BreakEventStrategy.PER_BLOCK, "money", "", 0, false).eventStrategy());
		}

		@Test
		@DisplayName("clearing enchants keep their configured strategy")
		void clearingKeepsStrategy() {
			assertEquals(BreakEventStrategy.PER_BLOCK,
					AreaBreakSettings.fromJson(json("{\"removeBlocks\":true,\"eventStrategy\":\"PER_BLOCK\"}"),
							BreakEventStrategy.AGGREGATE).eventStrategy());
		}

		@Test
		@DisplayName("withMessage replaces only the message")
		void withMessageReplacesOnlyMessage() {
			AreaBreakSettings original = AreaBreakSettings.fromJson(
					json("{\"countBlocksBroken\":true,\"currencyToGive\":\"gems\",\"message\":\"proc\",\"maxBlocks\":7,\"removeBlocks\":false}"),
					BreakEventStrategy.AGGREGATE);
			AreaBreakSettings remapped = original.withMessage("earnings");

			assertEquals("earnings", remapped.message());
			assertEquals(original.countBlocksBroken(), remapped.countBlocksBroken());
			assertEquals(original.eventStrategy(), remapped.eventStrategy());
			assertEquals(original.currencyToGive(), remapped.currencyToGive());
			assertEquals(original.maxBlocks(), remapped.maxBlocks());
			assertEquals(original.removeBlocks(), remapped.removeBlocks());
			assertEquals("proc", original.message(), "the original must be untouched");
		}
	}
}
