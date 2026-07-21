package dev.drawethree.xprison.api.enchants.area;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An immutable, inclusive axis-aligned cuboid — the region an {@link AreaBreakEnchant} is allowed
 * to affect.
 * <p>
 * This is deliberately a plain geometry type rather than a WorldGuard handle. X-Prison shades and
 * relocates its WorldGuard wrapper, so exposing {@code IWrappedRegion} across the API boundary
 * would not link for addons compiled against the unrelocated artifact. {@code AreaBounds} carries
 * only world + coordinates, so it is stable regardless of how either side shades its dependencies.
 * <p>
 * Both corners are <b>inclusive</b>: a bounds of {@code (0,0,0)-(0,0,0)} contains exactly one block
 * and has a {@link #volume()} of {@code 1}.
 *
 * @param world the world this cuboid belongs to
 * @param minX  the lowest X coordinate (inclusive)
 * @param minY  the lowest Y coordinate (inclusive)
 * @param minZ  the lowest Z coordinate (inclusive)
 * @param maxX  the highest X coordinate (inclusive)
 * @param maxY  the highest Y coordinate (inclusive)
 * @param maxZ  the highest Z coordinate (inclusive)
 * @since 1.9
 */
public record AreaBounds(@NotNull World world,
                         int minX, int minY, int minZ,
                         int maxX, int maxY, int maxZ) {

	/**
	 * Canonical constructor; normalises the corners so {@code min*} is never greater than
	 * {@code max*}. Callers may therefore pass the two corners in any order.
	 *
	 * @throws NullPointerException if {@code world} is {@code null}
	 */
	public AreaBounds {
		if (world == null) {
			throw new NullPointerException("world");
		}
		int lowX = Math.min(minX, maxX), highX = Math.max(minX, maxX);
		int lowY = Math.min(minY, maxY), highY = Math.max(minY, maxY);
		int lowZ = Math.min(minZ, maxZ), highZ = Math.max(minZ, maxZ);
		minX = lowX; maxX = highX;
		minY = lowY; maxY = highY;
		minZ = lowZ; maxZ = highZ;
	}

	/**
	 * Builds bounds from two corner locations, in any order.
	 *
	 * @param first  one corner
	 * @param second the opposite corner
	 * @return the normalised cuboid spanning both corners
	 * @throws IllegalArgumentException if the locations are in different (or null) worlds
	 */
	@NotNull
	public static AreaBounds between(@NotNull Location first, @NotNull Location second) {
		World world = first.getWorld();
		if (world == null || !world.equals(second.getWorld())) {
			throw new IllegalArgumentException("Both corners must belong to the same non-null world");
		}
		return new AreaBounds(world,
				first.getBlockX(), first.getBlockY(), first.getBlockZ(),
				second.getBlockX(), second.getBlockY(), second.getBlockZ());
	}

	/**
	 * @param x block X
	 * @param y block Y
	 * @param z block Z
	 * @return {@code true} if the block coordinate lies inside this cuboid
	 */
	public boolean contains(int x, int y, int z) {
		return x >= minX && x <= maxX
				&& y >= minY && y <= maxY
				&& z >= minZ && z <= maxZ;
	}

	/**
	 * @param location the location to test; {@code null} or a different world returns {@code false}
	 * @return {@code true} if the location lies inside this cuboid
	 */
	public boolean contains(@Nullable Location location) {
		return location != null
				&& world.equals(location.getWorld())
				&& contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	/**
	 * @param block the block to test; {@code null} or a different world returns {@code false}
	 * @return {@code true} if the block lies inside this cuboid
	 */
	public boolean contains(@Nullable Block block) {
		return block != null
				&& world.equals(block.getWorld())
				&& contains(block.getX(), block.getY(), block.getZ());
	}

	/**
	 * @return the number of block positions enclosed (always at least 1)
	 */
	public long volume() {
		return (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
	}
}
