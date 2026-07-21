package dev.drawethree.xprison.api.enchants.area;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AreaBoundsTest {

	private static final World WORLD = Mockito.mock(World.class);
	private static final World OTHER_WORLD = Mockito.mock(World.class);

	@Test
	@DisplayName("corners are normalised regardless of the order they are given in")
	void normalisesCorners() {
		AreaBounds bounds = new AreaBounds(WORLD, 10, 20, 30, -5, -6, -7);

		assertEquals(-5, bounds.minX());
		assertEquals(-6, bounds.minY());
		assertEquals(-7, bounds.minZ());
		assertEquals(10, bounds.maxX());
		assertEquals(20, bounds.maxY());
		assertEquals(30, bounds.maxZ());
	}

	@Test
	@DisplayName("both corners are inclusive")
	void cornersAreInclusive() {
		AreaBounds bounds = new AreaBounds(WORLD, 0, 0, 0, 2, 2, 2);

		assertTrue(bounds.contains(0, 0, 0));
		assertTrue(bounds.contains(2, 2, 2));
		assertTrue(bounds.contains(1, 1, 1));
		assertFalse(bounds.contains(3, 1, 1));
		assertFalse(bounds.contains(-1, 1, 1));
	}

	@Test
	@DisplayName("a single-block cuboid has volume 1")
	void singleBlockVolume() {
		assertEquals(1L, new AreaBounds(WORLD, 5, 5, 5, 5, 5, 5).volume());
	}

	@Test
	@DisplayName("volume counts every enclosed position")
	void volumeCountsInclusive() {
		assertEquals(27L, new AreaBounds(WORLD, 0, 0, 0, 2, 2, 2).volume());
		assertEquals(60L * 40L * 60L, new AreaBounds(WORLD, 0, 0, 0, 59, 39, 59).volume());
	}

	@Test
	@DisplayName("a location in another world is never contained")
	void rejectsOtherWorld() {
		AreaBounds bounds = new AreaBounds(WORLD, 0, 0, 0, 10, 10, 10);

		assertTrue(bounds.contains(new Location(WORLD, 5, 5, 5)));
		assertFalse(bounds.contains(new Location(OTHER_WORLD, 5, 5, 5)));
		assertFalse(bounds.contains((Location) null));
	}

	@Test
	@DisplayName("between() spans two corners in any order")
	void betweenSpansCorners() {
		AreaBounds bounds = AreaBounds.between(new Location(WORLD, 8, 9, 10), new Location(WORLD, 1, 2, 3));

		assertEquals(1, bounds.minX());
		assertEquals(10, bounds.maxZ());
		assertTrue(bounds.contains(4, 5, 6));
	}

	@Test
	@DisplayName("between() rejects corners from different worlds")
	void betweenRejectsMixedWorlds() {
		assertThrows(IllegalArgumentException.class,
				() -> AreaBounds.between(new Location(WORLD, 0, 0, 0), new Location(OTHER_WORLD, 1, 1, 1)));
	}

	@Test
	@DisplayName("a null world is rejected outright")
	void rejectsNullWorld() {
		assertThrows(NullPointerException.class, () -> new AreaBounds(null, 0, 0, 0, 1, 1, 1));
	}
}
